package com.netra.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditLogger {

    public void logSuccess(String domain, HttpMethod method, String url, HttpHeaders headers,
                           String body, Object response, long durationMs) {
        log.info("Success [{}] {} | {}ms\nHeaders: {}\nBody: {}\nResponse: {}",
                domain, method, durationMs, headers, body, response);
    }

    public void logFailure(String domain, HttpMethod method, String url, HttpHeaders headers,
                           String body, Exception e, long durationMs) {
        log.warn("Failure [{}] {} | {}ms\nHeaders: {}\nBody: {}\nError: {}",
                domain, method, durationMs, headers, body, e.getMessage(), e);
    }
}

