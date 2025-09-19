package com.netra.commons.validators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netra.commons.models.endpoint.*;
import com.netra.commons.validators.annotations.ValidEndpointConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndpointConfigValidator implements ConstraintValidator<ValidEndpointConfig, EndpointConfig> {

    @Override
    public boolean isValid(EndpointConfig config, ConstraintValidatorContext context) {
        if (config == null) return false;

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        // ==== Network ====
        NetworkConfig network = config.getNetwork();
        if (network == null) {
            context.buildConstraintViolationWithTemplate("Network configuration must be provided.")
                    .addPropertyNode("network").addConstraintViolation();
            return false;
        }

        if (network.getBaseUrl() == null || network.getBaseUrl().isBlank()) {
            context.buildConstraintViolationWithTemplate("Network baseUrl must not be empty.")
                    .addPropertyNode("network.baseUrl").addConstraintViolation();
            valid = false;
        }

        if (network.getTimeoutMillis() <= 0) {
            context.buildConstraintViolationWithTemplate("Timeout must be > 0.")
                    .addPropertyNode("network.timeoutMillis").addConstraintViolation();
            valid = false;
        }

        if (network.isUseProxy()) {
            ProxyConfig proxy = network.getProxy();
            if (proxy == null) {
                context.buildConstraintViolationWithTemplate("Proxy config must be provided when useProxy=true.")
                        .addPropertyNode("network.proxy").addConstraintViolation();
                valid = false;
            } else {
                if (proxy.getHost() == null || proxy.getHost().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Proxy host must not be blank.")
                            .addPropertyNode("network.proxy.host").addConstraintViolation();
                    valid = false;
                }
                if (proxy.getPort() == null || proxy.getPort() <= 0 || proxy.getPort() > 65535) {
                    context.buildConstraintViolationWithTemplate("Proxy port must be between 1–65535.")
                            .addPropertyNode("network.proxy.port").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== Security ====
        SecurityConfig security = config.getSecurity();
        if (security != null) {
            if (security.getAuthConfigs() != null) {
                for (int i = 0; i < security.getAuthConfigs().size(); i++) {
                    AuthConfig auth = security.getAuthConfigs().get(i);
                    if (auth == null) {
                        context.buildConstraintViolationWithTemplate("AuthConfig must not be null.")
                                .addPropertyNode("security.authConfigs[" + i + "]").addConstraintViolation();
                        valid = false;
                        continue;
                    }
                    if (auth.getAuthType() == null) {
                        context.buildConstraintViolationWithTemplate("AuthConfig type must not be null.")
                                .addPropertyNode("security.authConfigs[" + i + "].type").addConstraintViolation();
                        valid = false;
                    }
                }
            }

            EncryptionConfig enc = security.getEncryption();
            if (enc != null && enc.getType() != EncryptionConfig.EncryptionType.NONE) {
                if (enc.getEncryptionKey() == null || enc.getEncryptionKey().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Encryption key must be provided when encryption is enabled.")
                            .addPropertyNode("security.encryption.encryptionKey").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== Resilience ====
        ResilienceConfig resilience = config.getResilience();
        if (resilience != null) {
            CircuitBreakerConfig cb = resilience.getCircuitBreaker();
            if (cb != null) {
                if (cb.getFailureThreshold() <= 0) {
                    context.buildConstraintViolationWithTemplate("CircuitBreaker failureThreshold must be > 0.")
                            .addPropertyNode("resilience.circuitBreaker.failureThreshold").addConstraintViolation();
                    valid = false;
                }
                if (cb.getResetTimeoutMillis() <= 0) {
                    context.buildConstraintViolationWithTemplate("CircuitBreaker resetTimeoutMillis must be > 0.")
                            .addPropertyNode("resilience.circuitBreaker.resetTimeoutMillis").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== Endpoints ====
        Map<EndpointConfig.OperationType, EndpointDetail> endpoints = config.getEndpoints();
        if (endpoints == null || endpoints.isEmpty()) {
            context.buildConstraintViolationWithTemplate("At least one endpoint must be defined.")
                    .addPropertyNode("endpoints").addConstraintViolation();
            valid = false;
        } else {
            for (Map.Entry<EndpointConfig.OperationType, EndpointDetail> entry : endpoints.entrySet()) {
                String key = entry.getKey().name();
                if (!validateEndpointDetail(context, entry.getValue(), "endpoints[" + key + "]")) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    private boolean validateEndpointDetail(ConstraintValidatorContext context, EndpointDetail detail, String path) {
        if (detail == null) return true;
        boolean valid = true;

        if (detail.getUrl() == null || detail.getUrl().isBlank()) {
            context.buildConstraintViolationWithTemplate(path + ".url must not be blank.")
                    .addPropertyNode(path + ".url").addConstraintViolation();
            valid = false;
        }
        if (detail.getMethod() == null) {
            context.buildConstraintViolationWithTemplate(path + ".method must be specified.")
                    .addPropertyNode(path + ".method").addConstraintViolation();
            valid = false;
        }

        // ==== Static Headers ====
        if (detail.getHeaders() != null) {
            for (int i = 0; i < detail.getHeaders().size(); i++) {
                StaticHeader header = detail.getHeaders().get(i);
                if (header.getName() == null || header.getName().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Header name must not be blank in " + path)
                            .addPropertyNode(path + ".headers[" + i + "].name").addConstraintViolation();
                    valid = false;
                }
                if (header.getValue() == null || header.getValue().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Header value must not be blank in " + path)
                            .addPropertyNode(path + ".headers[" + i + "].value").addConstraintViolation();
                    valid = false;
                }
                if (header.isSecret()) {
                    if (!header.getValue().matches(".*\\$\\{vault:[^}]+}.*")) {
                        context.buildConstraintViolationWithTemplate("Secret header must contain vault placeholder in " + path)
                                .addPropertyNode(path + ".headers[" + i + "].value").addConstraintViolation();
                        valid = false;
                    }
                } else {
                    if (header.getValue().matches(".*\\$\\{vault:[^}]+}.*")) {
                        context.buildConstraintViolationWithTemplate("Non-secret header must not contain vault placeholders in " + path)
                                .addPropertyNode(path + ".headers[" + i + "].value").addConstraintViolation();
                        valid = false;
                    }
                }
            }
        }

        // ==== Dynamic Headers ====
        if (detail.getDynamicHeaders() != null) {
            for (int i = 0; i < detail.getDynamicHeaders().size(); i++) {
                DynamicHeader dyn = detail.getDynamicHeaders().get(i);
                if (dyn.getName() == null || dyn.getName().isBlank()) {
                    context.buildConstraintViolationWithTemplate("DynamicHeader name must not be blank in " + path)
                            .addPropertyNode(path + ".dynamicHeaders[" + i + "].name").addConstraintViolation();
                    valid = false;
                }
                if (dyn.getDescription() != null && dyn.getDescription().isBlank()) {
                    context.buildConstraintViolationWithTemplate("DynamicHeader description, if provided, must not be blank in " + path)
                            .addPropertyNode(path + ".dynamicHeaders[" + i + "].description").addConstraintViolation();
                    valid = false;
                }
                // dynamic headers should not contain vault placeholders
                if (dyn.getName() != null && dyn.getName().contains("${vault:")) {
                    context.buildConstraintViolationWithTemplate("DynamicHeader name must not contain vault placeholders in " + path)
                            .addPropertyNode(path + ".dynamicHeaders[" + i + "].name").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== Body Template ====
        if (detail.getRequestBodyTemplate() != null) {
            try {
                validateTemplate(detail.getRequestBodyTemplate());
            } catch (IllegalArgumentException e) {
                context.buildConstraintViolationWithTemplate("Invalid requestBodyTemplate in " + path + ": " + e.getMessage())
                        .addPropertyNode(path + ".requestBodyTemplate").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    public static void validateTemplate(String template) {
        if (template == null) throw new IllegalArgumentException("Template cannot be null");

        try {
            new ObjectMapper().readTree(template);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON structure in template", e);
        }

        Pattern stringPlaceholderPattern = Pattern.compile("\"\\$\\{[a-zA-Z0-9_]+}\"");
        Pattern rawPlaceholderPattern = Pattern.compile("(:\\s*\\$[a-zA-Z0-9_]+)");

        Matcher stringMatcher = stringPlaceholderPattern.matcher(template);
        Matcher rawMatcher = rawPlaceholderPattern.matcher(template);

        if (template.contains("${") && !stringMatcher.find()) {
            throw new IllegalArgumentException("Found string placeholder without proper quotes: " + template);
        }

        if (template.contains("$") && !rawMatcher.find()) {
            throw new IllegalArgumentException("Found raw placeholder used incorrectly: " + template);
        }
    }

    public static boolean isValid(ApiKeyAuth auth, ConstraintValidatorContext context, String path) {
        boolean valid = true;

        if (auth == null) return true;

        // ==== Vault alias must be provided ====
        if (auth.getApiKey() == null || auth.getApiKey().isBlank()) {
            context.buildConstraintViolationWithTemplate("apiKey must not be blank.")
                    .addPropertyNode(path + ".apiKey").addConstraintViolation();
            valid = false;
        }

        //todo: only used maybe after substitution
//        else if (!auth.getApiKey().startsWith("vault:")) {
//            context.buildConstraintViolationWithTemplate("apiKeyVaultAlias must start with 'vault:'.")
//                    .addPropertyNode(path + ".apiKeyVaultAlias").addConstraintViolation();
//            valid = false;
//        }

        // ==== Location must be clear ====
        if (auth.isAsQueryParam()) {
            if (auth.getQueryParamName() == null || auth.getQueryParamName().isBlank()) {
                context.buildConstraintViolationWithTemplate("queryParamName must be provided when asQueryParam is true.")
                        .addPropertyNode(path + ".queryParamName").addConstraintViolation();
                valid = false;
            }
        } else {
            if (auth.getHeaderName() == null || auth.getHeaderName().isBlank()) {
                context.buildConstraintViolationWithTemplate("headerName must be provided when asQueryParam is false.")
                        .addPropertyNode(path + ".headerName").addConstraintViolation();
                valid = false;
            }
        }

        // ==== Prefix / Suffix sanity checks ====
        if (auth.getPrefix() != null && !auth.getPrefix().isBlank()) {
            if (auth.getPrefixSeparator() == null) {
                context.buildConstraintViolationWithTemplate("prefixSeparator must not be null when prefix is set.")
                        .addPropertyNode(path + ".prefixSeparator").addConstraintViolation();
                valid = false;
            }
        }

        if (auth.getSuffix() != null && !auth.getSuffix().isBlank()) {
            if (auth.getSuffixSeparator() == null) {
                context.buildConstraintViolationWithTemplate("suffixSeparator must not be null when suffix is set.")
                        .addPropertyNode(path + ".suffixSeparator").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    public static boolean validate(BasicAuth auth, ConstraintValidatorContext context) {
        if (auth == null) return false;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        // ==== passwordVaultAlias ====
        if (auth.getPassword() == null || auth.getPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("BasicAuth.passwordVaultAlias must not be blank")
                    .addPropertyNode("passwordVaultAlias").addConstraintViolation();
            valid = false;
        }

        //todo: maybe not use
//        else if (!auth.getPassword().startsWith("vault:")) {
//            context.buildConstraintViolationWithTemplate("BasicAuth.passwordVaultAlias must be a vault reference (prefix 'vault:')")
//                    .addPropertyNode("passwordVaultAlias").addConstraintViolation();
//            valid = false;
//        }

        // ==== username (optional, but allow empty string) ====
        if (auth.getUsername() != null && auth.getUsername().contains(":")) {
            context.buildConstraintViolationWithTemplate("BasicAuth.username must not contain ':' character")
                    .addPropertyNode("username").addConstraintViolation();
            valid = false;
        }

        // ==== headerName ====
        if (auth.getHeaderName() == null || auth.getHeaderName().isBlank()) {
            context.buildConstraintViolationWithTemplate("BasicAuth.headerName must not be blank")
                    .addPropertyNode("headerName").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    public static boolean validate(BearerTokenAuth auth, ConstraintValidatorContext context) {
        if (auth == null) return false;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        // ==== tokenVaultAlias ====
        if (auth.getToken() == null || auth.getToken().isBlank()) {
            context.buildConstraintViolationWithTemplate("BearerTokenAuth.token must not be blank")
                    .addPropertyNode("tokenVault").addConstraintViolation();
            valid = false;
        }
        //todo: may be skip
//        else if (!auth.getToken().startsWith("vault:")) {
//            context.buildConstraintViolationWithTemplate("BearerTokenAuth.tokenVaultAlias must be a vault reference (prefix 'vault:')")
//                    .addPropertyNode("tokenVaultAlias").addConstraintViolation();
//            valid = false;
//        }

        // ==== headerName ====
        if (auth.getHeaderName() == null || auth.getHeaderName().isBlank()) {
            context.buildConstraintViolationWithTemplate("BearerTokenAuth.headerName must not be blank")
                    .addPropertyNode("headerName").addConstraintViolation();
            valid = false;
        }

        // ==== prefix ====
        if (auth.getPrefix() == null) {
            context.buildConstraintViolationWithTemplate("BearerTokenAuth.prefix must not be null (can be empty string if no prefix is required)")
                    .addPropertyNode("prefix").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    public boolean isValid(EncryptionConfig.AadHeader header, ConstraintValidatorContext context) {
        if (header == null) return true; // allow null, handled upstream

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        // ==== Name ====
        if (header.getName() == null || header.getName().isBlank()) {
            context.buildConstraintViolationWithTemplate("AAD header name must not be blank.")
                    .addPropertyNode("name").addConstraintViolation();
            valid = false;
        }

        // ==== Mutually exclusive secret & dynamic ====
        if (header.isSecret() && header.isDynamic()) {
            context.buildConstraintViolationWithTemplate("AAD header cannot be both secret and dynamic.")
                    .addConstraintViolation();
            valid = false;
        }

        // ==== Secret headers ====
        if (header.isSecret()) {
            if (header.getValue() == null || !header.getValue().startsWith("vault:")) {
                context.buildConstraintViolationWithTemplate(
                                "AAD secret header must reference a vault alias (e.g., vault:myKey).")
                        .addPropertyNode("value").addConstraintViolation();
                valid = false;
            }
        }

        // ==== Dynamic headers ====
        if (header.isDynamic()) {
            if (header.getValue() == null || !header.getValue().matches("\\$\\{[a-zA-Z0-9._-]+\\}")) {
                context.buildConstraintViolationWithTemplate(
                                "AAD dynamic header must contain a placeholder like ${requestId}.")
                        .addPropertyNode("value").addConstraintViolation();
                valid = false;
            }
        }

        // ==== Static headers ====
        if (!header.isSecret() && !header.isDynamic()) {
            if (header.getValue() == null || header.getValue().isBlank()) {
                context.buildConstraintViolationWithTemplate(
                                "AAD static header must have a literal non-empty value.")
                        .addPropertyNode("value").addConstraintViolation();
                valid = false;
            }
            if (header.getValue().matches(".*\\$\\{[^}]+\\}.*")) {
                context.buildConstraintViolationWithTemplate(
                                "AAD static header must not contain placeholders.")
                        .addPropertyNode("value").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    public boolean isValid(EncryptionConfig config, ConstraintValidatorContext context) {
        if (config == null) return true; // optional section

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        // ==== Type ====
        EncryptionConfig.EncryptionType type = config.getType();
        if (type == null) {
            context.buildConstraintViolationWithTemplate("Encryption type must not be null.")
                    .addPropertyNode("type").addConstraintViolation();
            return false;
        }

        if (type != EncryptionConfig.EncryptionType.NONE) {
            // ==== Algorithm ====
            if (config.getAlgorithm() == null || config.getAlgorithm().isBlank()) {
                context.buildConstraintViolationWithTemplate("Algorithm must be provided when encryption is enabled.")
                        .addPropertyNode("algorithm").addConstraintViolation();
                valid = false;
            }

            // ==== Encryption Key ====
            if (config.getEncryptionKey() == null || !config.getEncryptionKey().startsWith("vault:")) {
                context.buildConstraintViolationWithTemplate("Encryption key must be a valid vault alias (vault:...).")
                        .addPropertyNode("encryptionKey").addConstraintViolation();
                valid = false;
            }

            // ==== IV Param (AES/JWE usually require) ====
            if (type == EncryptionConfig.EncryptionType.AES || type == EncryptionConfig.EncryptionType.JWE) {
                if (config.getIvParam() == null || config.getIvParam().isBlank()) {
                    context.buildConstraintViolationWithTemplate("IV parameter must be provided for AES/JWE encryption.")
                            .addPropertyNode("ivParam").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== AAD Headers ====
        List<EncryptionConfig.AadHeader> aadHeaders = config.getAadHeaders();
        if (aadHeaders != null) {
            for (int i = 0; i < aadHeaders.size(); i++) {
                EncryptionConfig.AadHeader header = aadHeaders.get(i);
                //AadHeaderValidator headerValidator = new AadHeaderValidator();
                if (!isValid(header, context)) {
                    context.buildConstraintViolationWithTemplate("Invalid AAD header at index " + i)
                            .addPropertyNode("aadHeaders[" + i + "]").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // ==== Signature Key ====
        if (config.isSignPayload()) {
            if (config.getSignatureKey() == null || !config.getSignatureKey().startsWith("vault:")) {
                context.buildConstraintViolationWithTemplate(
                                "Signature key must be provided as a vault alias when signPayload is enabled.")
                        .addPropertyNode("signatureKey").addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    public boolean isValid(MtlsAuth auth, ConstraintValidatorContext context) {
        if (auth == null) return false;

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        // ==== Client certificate ====
        if (auth.getCertVaultAlias() == null || !auth.getCertVaultAlias().startsWith("vault:")) {
            context.buildConstraintViolationWithTemplate("certVaultAlias must be a valid vault reference")
                    .addPropertyNode("certVaultAlias").addConstraintViolation();
            valid = false;
        }

        // ==== Client key ====
        if (auth.getKeyVaultAlias() == null || !auth.getKeyVaultAlias().startsWith("vault:")) {
            context.buildConstraintViolationWithTemplate("keyVaultAlias must be a valid vault reference")
                    .addPropertyNode("keyVaultAlias").addConstraintViolation();
            valid = false;
        }

        // ==== Optional key password ====
        if (auth.getKeyPasswordVaultAlias() != null && !auth.getKeyPasswordVaultAlias().startsWith("vault:")) {
            context.buildConstraintViolationWithTemplate("keyPasswordVaultAlias must be a vault reference if provided")
                    .addPropertyNode("keyPasswordVaultAlias").addConstraintViolation();
            valid = false;
        }

        // ==== Trust store ====
        if (auth.getTrustStoreVaultAlias() != null) {
            if (!auth.getTrustStoreVaultAlias().startsWith("vault:")) {
                context.buildConstraintViolationWithTemplate("trustStoreVaultAlias must be a vault reference")
                        .addPropertyNode("trustStoreVaultAlias").addConstraintViolation();
                valid = false;
            }

            if (auth.getTrustStorePasswordAlias() == null || !auth.getTrustStorePasswordAlias().startsWith("vault:")) {
                context.buildConstraintViolationWithTemplate("trustStorePasswordAlias must be provided as a vault reference when trustStoreVaultAlias is set")
                        .addPropertyNode("trustStorePasswordAlias").addConstraintViolation();
                valid = false;
            }

            if (auth.getTrustStoreType() == null || auth.getTrustStoreType().isBlank()) {
                context.buildConstraintViolationWithTemplate("trustStoreType must be provided when trustStoreVaultAlias is set")
                        .addPropertyNode("trustStoreType").addConstraintViolation();
                valid = false;
            }
        }

        // ==== Optional file names ====
        if (auth.getKeyStoreFileName() != null && auth.getKeyStoreFileName().isBlank()) {
            context.buildConstraintViolationWithTemplate("keyStoreFileName cannot be blank if provided")
                    .addPropertyNode("keyStoreFileName").addConstraintViolation();
            valid = false;
        }

        if (auth.getTrustKeyFileName() != null && auth.getTrustKeyFileName().isBlank()) {
            context.buildConstraintViolationWithTemplate("trustKeyFileName cannot be blank if provided")
                    .addPropertyNode("trustKeyFileName").addConstraintViolation();
            valid = false;
        }

        return valid;
    }


}
