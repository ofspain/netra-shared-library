package com.netra.commons.validators;

import com.netra.commons.models.EndpointConfig;
import com.netra.commons.models.EndpointConfig.EndpointDetail;
import com.netra.commons.models.EndpointConfig.ProxyConfig;
import com.netra.commons.models.EndpointConfig.EndpointHeader;
import com.netra.commons.validators.annotations.ValidEndpointConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class EndpointConfigValidator implements ConstraintValidator<ValidEndpointConfig, EndpointConfig> {

    @Override
    public boolean isValid(EndpointConfig config, ConstraintValidatorContext context) {
        if (config == null) return false;

        context.disableDefaultConstraintViolation();
        boolean valid = true;

        // Base URL
        if (config.getBaseUrl() == null || config.getBaseUrl().isBlank()) {
            context.buildConstraintViolationWithTemplate("Base URL must not be empty.")
                    .addPropertyNode("baseUrl").addConstraintViolation();
            valid = false;
        }

        // Timeout
        if (config.getTimeoutMillis() <= 0) {
            context.buildConstraintViolationWithTemplate("Timeout must be a positive number.")
                    .addPropertyNode("timeoutMillis").addConstraintViolation();
            valid = false;
        }

        // Proxy Config
        if (config.isUseProxy()) {
            ProxyConfig proxy = config.getProxy();
            if (proxy == null) {
                context.buildConstraintViolationWithTemplate("Proxy configuration must be provided when proxy is enabled.")
                        .addPropertyNode("proxy").addConstraintViolation();
                valid = false;
            } else {
                if (proxy.getHost() == null || proxy.getHost().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Proxy host must not be empty.")
                            .addPropertyNode("proxy.host").addConstraintViolation();
                    valid = false;
                }
                if (proxy.getPort() == null || proxy.getPort() <= 0 || proxy.getPort() > 65535) {
                    context.buildConstraintViolationWithTemplate("Proxy port must be between 1 and 65535.")
                            .addPropertyNode("proxy.port").addConstraintViolation();
                    valid = false;
                }
            }
        }

        // Endpoint Details
        if (!validateEndpointDetail(context, config.getUniqueTransaction(), "uniqueTransaction")) valid = false;
        if (!validateEndpointDetail(context, config.getMultipleTransaction(), "multipleTransaction")) valid = false;

        return valid;
    }

    private boolean validateEndpointDetail(ConstraintValidatorContext context, EndpointDetail detail, String path) {
        boolean valid = true;

        if (detail == null) return true; // Optional

        if (detail.getUrl() == null || detail.getUrl().isBlank()) {
            context.buildConstraintViolationWithTemplate(path + ".url must not be blank.")
                    .addPropertyNode(path + ".url").addConstraintViolation();
            valid = false;
        }

        if (detail.getMethod() == null) {
            context.buildConstraintViolationWithTemplate(path + ".method must be specified (GET or POST).")
                    .addPropertyNode(path + ".method").addConstraintViolation();
            valid = false;
        }

        List<EndpointHeader> headers = detail.getHeaders();
        if (headers != null) {
            for (EndpointHeader header : headers) {
                if (header.getName() == null || header.getName().isBlank()) {
                    context.buildConstraintViolationWithTemplate("Header name must not be blank in " + path)
                            .addPropertyNode(path + ".headers").addConstraintViolation();
                    valid = false;
                }
            }
        }

        return valid;
    }
}
