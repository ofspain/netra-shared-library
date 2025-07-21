package com.netra.commons.util;

import com.netra.commons.models.EndpointConfig;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@UtilityClass
public class UriBuilderUtil {

    public String resolveUrl(String baseUrl, String relativeUrl, Map<String, String> pathParams, Map<String, String> queryParams) {
        String resolvedPath = relativeUrl;
        if (pathParams != null) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                resolvedPath = resolvedPath.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + resolvedPath);
        if (queryParams != null) {
            queryParams.forEach(builder::queryParam);
        }

        return builder.toUriString();
    }

    public HttpHeaders buildHeaders(List<EndpointConfig.EndpointHeader> headers, Map<String, String> dynamicHeaderValues) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (headers != null) {
            for (EndpointConfig.EndpointHeader header : headers) {
                if (header.isDynamic()) {
                    String value = dynamicHeaderValues.get(header.getName());
                    if (value == null) {
                        throw new IllegalArgumentException("Missing dynamic header: " + header.getName());
                    }
                    httpHeaders.add(header.getName(), value);
                } else {
                    httpHeaders.add(header.getName(), header.getValue());
                }
            }
        }

        return httpHeaders;
    }

    public String resolveRequestBody(String template, Map<String, String> context) {
        if (template == null) return null;
        for (Map.Entry<String, String> entry : context.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            template = template.replace(placeholder, entry.getValue());
        }
        return template;
    }
}