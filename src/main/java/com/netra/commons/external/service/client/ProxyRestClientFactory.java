package com.netra.commons.external.service.client;

import com.netra.commons.models.EndpointConfig;
import com.netra.commons.util.SecretCryptoUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProxyRestClientFactory {

    private final SecretCryptoUtil cryptoUtil;
    private final Validator validator;


    /**
     * Builds a RestClient configured with proxy settings from the provided EndpointConfig.
     *
     * @param config EndpointConfig containing proxy configuration
     * @return RestClient configured with proxy settings
     * @throws IllegalArgumentException if Endpoint configuration is invalid
     */
    public RestClient buildProxiedRestClient(EndpointConfig config) {
        Set<ConstraintViolation<EndpointConfig>> violations = validator.validate(config);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid config: " + errorMessages);
        }

        EndpointConfig.ProxyConfig proxyConfig = config.getProxy();

        String host = proxyConfig.getHost();
        int port = proxyConfig.getPort();
        String username = proxyConfig.getUsername();
        String encryptedPassword = proxyConfig.getPassword();

        log.info("Building RestClient with for config {}",config);

        String decryptedPassword = null;
        if (encryptedPassword != null && !encryptedPassword.isBlank()) {
            try {
                decryptedPassword = cryptoUtil.decrypt(encryptedPassword);
            } catch (Exception e) {
                log.error("Failed to decrypt proxy password", e);
                throw new IllegalStateException("Failed to decrypt proxy password", e);
            }
        }

        return createRestClientWithProxy(
                host, port, username, decryptedPassword,
                config.getTimeoutMillis(), config.getTimeoutMillis()
        );
    }

    /**
     * Creates a RestClient with proxy configuration using Apache HttpClient 5.
     */
    private RestClient createRestClientWithProxy(String proxyHost, int proxyPort,
                                                 String proxyUsername, String proxyPassword,
                                                 int connectTimeoutMs, int readTimeoutMs) {

        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        log.debug("Configuring proxy: {}", proxy);

        // Configure timeouts using Timeout.ofMilliseconds for HttpClient 5
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeoutMs))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(readTimeoutMs))
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeoutMs))
                .setProxy(proxy)
                .build();

        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig);

        // Configure proxy authentication if credentials are provided
        if (proxyUsername != null && !proxyUsername.isBlank() && proxyPassword != null) {
            log.debug("Configuring proxy authentication for user: {}", proxyUsername);

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            ((BasicCredentialsProvider) credentialsProvider).setCredentials(
                    new AuthScope(proxyHost, proxyPort),
                    new UsernamePasswordCredentials(proxyUsername, proxyPassword.toCharArray())
            );

            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            setSystemProxyAuthenticator(proxyHost, proxyPort, proxyUsername, proxyPassword);
        }

        CloseableHttpClient httpClient = clientBuilder.build();

        // Use HttpComponentsClientHttpRequestFactory for RestClient compatibility
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Set additional timeouts on the request factory
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setConnectionRequestTimeout(readTimeoutMs);

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    /**
     * Configures system-wide proxy settings and authenticator.
     * This is needed for some libraries that don't use the HttpClient directly.
     */
    private void setSystemProxyAuthenticator(String proxyHost, int proxyPort,
                                             String username, String password) {
        log.debug("Setting system proxy properties");

        // Set system properties for HTTP and HTTPS proxy
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", String.valueOf(proxyPort));
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", String.valueOf(proxyPort));

        if (username != null && !username.isBlank() && password != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (getRequestorType() == RequestorType.PROXY) {
                        String requestingHost = getRequestingHost();
                        int requestingPort = getRequestingPort();

                        // Only provide credentials for the configured proxy
                        if (proxyHost.equals(requestingHost) && proxyPort == requestingPort) {
                            return new PasswordAuthentication(username, password.toCharArray());
                        }
                    }
                    return null;
                }
            });
        }
    }

    /**
     * Creates a RestClient without proxy for direct connections.
     * Useful for fallback scenarios or non-proxy endpoints.
     */
    public RestClient buildRestClientUnProxied(int timeoutMs) {
        log.info("Building direct RestClient (no proxy)");

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(timeoutMs))
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(timeoutMs))
                .setResponseTimeout(Timeout.ofMilliseconds(timeoutMs))
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        requestFactory.setConnectTimeout(timeoutMs);
        requestFactory.setConnectionRequestTimeout(timeoutMs);

        return RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }
}

/**
 * Enhanced Sample Usage Examples:
 *
 * @Autowired
 * private ProxyRestClientFactory proxyRestClientFactory;
 *
 * // Basic proxy usage
 * public void callWithProxy(EndpointConfig config) {
 *     try {
 *         RestClient client = proxyRestClientFactory.buildFor(config);
 *
 *         String response = client.get()
 *                 .uri(config.getBaseUrl() + "/api/resource")
 *                 .header("Content-Type", "application/json")
 *                 .retrieve()
 *                 .body(String.class);
 *
 *         log.info("Received response: {}", response);
 *     } catch (Exception e) {
 *         log.error("Error calling API through proxy", e);
 *         throw e;
 *     }
 * }
 *
 * // POST request with error handling
 * public ResponseEntity<MyResponse> postWithProxy(EndpointConfig config, MyRequest request) {
 *     RestClient client = proxyRestClientFactory.buildFor(config);
 *
 *     return client.post()
 *             .uri(config.getBaseUrl() + "/api/submit")
 *             .header("Content-Type", "application/json")
 *             .body(request)
 *             .retrieve()
 *             .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
 *                 throw new ClientException("Client error: " + res.getStatusCode());
 *             })
 *             .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
 *                 throw new ServerException("Server error: " + res.getStatusCode());
 *             })
 *             .toEntity(MyResponse.class);
 * }
 *
 * // Direct client usage (fallback)
 * public void callDirect() {
 *     RestClient directClient = proxyRestClientFactory.buildDirectClient(30000);
 *     // use directClient for non-proxy calls
 * }
 */