package com.netra.commons.external.service.client;

import com.netra.commons.models.EndpointConfig;
import com.netra.commons.util.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EndpointExecutorService {

    private final RedisTemplate<String, String> redisTemplate;
    private final MeterRegistry meterRegistry;
    private final TokenProvider tokenProvider; // Token retrieval logic
    private final AuditLogger auditLogger;

    //todo: Shall we test this on a stormy endpoint and sail for a sample config JSON + insert command?

    public <T> T executeRequest(
            RestClient client,
            EndpointConfig config,
            boolean isMultiple,
            Map<String, String> pathParams,
            Map<String, String> queryParams,
            Map<String, String> dynamicHeaderValues,
            Map<String, String> requestBodyContext,
            ParameterizedTypeReference<T> responseType
    ) {
        EndpointConfig.EndpointDetail detail = isMultiple ? config.getMultipleTransaction() : config.getUniqueTransaction();
        String resolvedUrl = UriBuilderUtil.resolveUrl(config.getBaseUrl(), detail.getUrl(), pathParams, queryParams);
        HttpMethod method = convertMethod(detail.getMethod());

        List<EndpointConfig.EndpointHeader> allHeaders = enrichWithAuthHeader(config, detail.getHeaders());
        HttpHeaders headers = UriBuilderUtil.buildHeaders(allHeaders, dynamicHeaderValues);

        String bodyContent = null;
        boolean hasBody = BasicUtil.validString(config.getRequestBodyTemplate());

        if (hasBody) {
            bodyContent = UriBuilderUtil.resolveRequestBody(config.getRequestBodyTemplate(), requestBodyContext);
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        // Start timer
        Timer.Sample sample = Timer.start(meterRegistry);
        long start = System.currentTimeMillis();
        try {
            var builder = client.method(method)
                    .uri(resolvedUrl)
                    .headers(h -> h.addAll(headers));

            RestClient.ResponseSpec response;
            if (hasBody) {
                response = builder.body(bodyContent).retrieve();
            } else {
                response = builder.retrieve();
            }

            T result = response.body(responseType);

            auditLogger.logSuccess(config.getDomainCode(), method, resolvedUrl, headers, bodyContent, result, System.currentTimeMillis() - start);
            sample.stop(meterRegistry.timer("external_call", "domain", config.getDomainCode(), "status", "success"));
            return result;

        } catch (Exception e) {
            auditLogger.logFailure(config.getDomainCode(), method, resolvedUrl, headers, bodyContent, e, System.currentTimeMillis() - start);
            sample.stop(meterRegistry.timer("external_call", "domain", config.getDomainCode(), "status", "failure"));
            throw e;
        }
    }




    public <T> T executeRequestWithResilience(
            RestClient client,
            EndpointConfig config,
            boolean isMultiple,
            Map<String, String> pathParams,
            Map<String, String> queryParams,
            Map<String, String> dynamicHeaderValues,
            Map<String, String> requestBodyContext,
            ParameterizedTypeReference<T> responseType,
            Class<T> responseClass
    ) {
        EndpointConfig.EndpointDetail detail = isMultiple ? config.getMultipleTransaction() : config.getUniqueTransaction();

        String resolvedUrl = UriBuilderUtil.resolveUrl(config.getBaseUrl(), detail.getUrl(), pathParams, queryParams);
        HttpMethod method = convertMethod(detail.getMethod());
        HttpHeaders headers = UriBuilderUtil.buildHeaders(detail.getHeaders(), dynamicHeaderValues);

        HttpEntity<?> entity;
        boolean hasBody = BasicUtil.validString(config.getRequestBodyTemplate());

        if (hasBody) {
            String body = UriBuilderUtil.resolveRequestBody(config.getRequestBodyTemplate(), requestBodyContext);
            headers.setContentType(MediaType.APPLICATION_JSON);
            entity = new HttpEntity<>(body, headers);
        } else {
            entity = new HttpEntity<>(headers);
        }

        RestClient.RequestBodySpec request = client
                .method(method)
                .uri(resolvedUrl)
                .headers(h -> h.addAll(headers));

        Supplier<T> execute = () -> {
            if (hasBody) {
                return request.body(entity.getBody()).retrieve().body(responseType);
            } else {
                return request.retrieve().body(responseType);
            }
        };

        ResilienceRegistryResolver resolver = new ResilienceRegistryResolver();
        Supplier<T> safeSupplier = resolver.decorateWithRetryAndFallback(
                config.getDomainCode() + "_" + (isMultiple ? "multi" : "single"),
                execute,
                config,
                responseClass
        );

        return safeSupplier.get();
    }


    private List<EndpointConfig.EndpointHeader> enrichWithAuthHeader(EndpointConfig config, List<EndpointConfig.EndpointHeader> headers) {
        if (!config.isRequiresAuth()) {
            return headers;
        }
        List<EndpointConfig.EndpointHeader> result = new ArrayList<>(headers != null ? headers : List.of());

        switch (config.getAuthType()) {
            case BEARER_TOKEN -> {
                String token = tokenProvider.getBearerToken(config.getDomainCode());
                result.add(new EndpointConfig.EndpointHeader("Authorization", "Bearer " + token, false));
            }
            case BASIC -> {
                String creds = tokenProvider.getBasicCredentials(config.getDomainCode());
                result.add(new EndpointConfig.EndpointHeader("Authorization", "Basic " + creds, false));
            }
            case API_KEY -> {
                result.add(new EndpointConfig.EndpointHeader("x-api-key", tokenProvider.getApiKey(config.getDomainCode()), false));
            }
            default -> {}
        }
        return result;
    }

    private HttpMethod convertMethod(EndpointConfig.HTTPMethod m) {
        return m == EndpointConfig.HTTPMethod.POST ? HttpMethod.POST : HttpMethod.GET;
    }
}
