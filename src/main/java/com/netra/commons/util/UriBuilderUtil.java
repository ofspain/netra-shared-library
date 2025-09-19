package com.netra.commons.util;

import com.netra.commons.models.endpoint.*;
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

//    public HttpHeaders buildHeaders(EndpointDetail endpointDetail) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        List<StaticHeader> staticHeaders = endpointDetail.getHeaders();
//        List<DynamicHeader> dynamicHeaders = endpointDetail.getDynamicHeaders();
//
//        if (headers != null) {
//            for (EndpointConfig_.EndpointHeader header : headers) {
//                if (header.isSecret()) {
//                    String value = secretHeaderValues.get(header.getName());
//                    if (value == null) {
//                        throw new IllegalArgumentException("Missing dynamic header: " + header.getName());
//                    }
//                    httpHeaders.add(header.getName(), value);
//                } else {
//                    httpHeaders.add(header.getName(), header.getValue());
//                }
//            }
//        }
//
//        return httpHeaders;
//    }

    public static String resolveStaticSecretTemplate(String template){
        //todo: resolve template here
        return "";
    }

    //builds the body of the request
    public static String resolveRequestBody(String template, Map<String, String> context) {
        if (template == null) {
            return null;
        }

        String resolved = template;

        for (Map.Entry<String, String> entry : context.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // String substitution: "${key}"
            String stringPlaceholder = "${" + key + "}";
            resolved = resolved.replace(stringPlaceholder, value);

            // Raw substitution: $key
            String rawPlaceholder = "$" + key;
            resolved = resolved.replace(rawPlaceholder, value);
        }

        // After replacements, check if any placeholder remains unresolved
        if (resolved.contains("${") || resolved.matches(".*\\$[a-zA-Z0-9_]+.*")) {
            throw new IllegalArgumentException("Unresolved placeholders found in request body: " + resolved);
        }

        return resolved;
    }


}