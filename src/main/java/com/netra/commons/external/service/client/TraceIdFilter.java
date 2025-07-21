package com.netra.commons.external.service.client;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceIdFilter is a servlet filter that generates and manages trace IDs for request correlation
 * across distributed services. It uses SLF4J's MDC (Mapped Diagnostic Context) to store
 * trace information that can be included in log statements.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TraceIdFilter implements Filter {

    // MDC keys for trace information
    private static final String TRACE_ID_KEY = "traceId";
    private static final String SPAN_ID_KEY = "spanId";
    private static final String USER_ID_KEY = "userId";
    private static final String REQUEST_URI_KEY = "requestUri";
    private static final String REQUEST_METHOD_KEY = "requestMethod";

    // HTTP headers for trace propagation
    private static final String X_TRACE_ID_HEADER = "X-Trace-Id";
    private static final String X_SPAN_ID_HEADER = "X-Span-Id";
    private static final String X_USER_ID_HEADER = "X-User-Id";

    // Alternative header names (for compatibility)
    private static final String TRACE_ID_HEADER_ALT = "traceId";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TraceIdFilter initialized - will add trace IDs to all requests");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            // Generate or extract trace ID
            String traceId = getOrGenerateTraceId(httpRequest);
            String spanId = generateSpanId();

            // Set trace information in MDC
            MDC.put(TRACE_ID_KEY, traceId);
            MDC.put(SPAN_ID_KEY, spanId);
            MDC.put(REQUEST_URI_KEY, httpRequest.getRequestURI());
            MDC.put(REQUEST_METHOD_KEY, httpRequest.getMethod());

            // Extract user information if available
            String userId = extractUserId(httpRequest);
            if (StringUtils.hasText(userId)) {
                MDC.put(USER_ID_KEY, userId);
            }

            // Add trace ID to response headers for downstream services
            httpResponse.setHeader(X_TRACE_ID_HEADER, traceId);
            httpResponse.setHeader(X_SPAN_ID_HEADER, spanId);

            log.debug("Request started - TraceId: {}, SpanId: {}, URI: {} {}",
                    traceId, spanId, httpRequest.getMethod(), httpRequest.getRequestURI());

            // Continue with the filter chain
            chain.doFilter(request, response);

        } finally {
            // Always clean up MDC to prevent memory leaks
            clearMDC();
        }
    }

    @Override
    public void destroy() {
        log.info("TraceIdFilter destroyed");
    }

    /**
     * Gets trace ID from request headers or generates a new one
     */
    private String getOrGenerateTraceId(HttpServletRequest request) {
        // Try to get trace ID from various header sources
        String traceId = request.getHeader(X_TRACE_ID_HEADER);

        if (!StringUtils.hasText(traceId)) {
            traceId = request.getHeader(TRACE_ID_HEADER_ALT);
        }

        if (!StringUtils.hasText(traceId)) {
            traceId = request.getHeader(CORRELATION_ID_HEADER);
        }

        // If no trace ID found in headers, generate a new one
        if (!StringUtils.hasText(traceId)) {
            traceId = generateUniqueId();
        }

        return traceId;
    }

    /**
     * Extracts user ID from request headers or parameters
     */
    private String extractUserId(HttpServletRequest request) {
        // Try different sources for user ID
        String userId = request.getHeader(X_USER_ID_HEADER);

        if (!StringUtils.hasText(userId)) {
            userId = request.getHeader("X-User");
        }

        if (!StringUtils.hasText(userId)) {
            userId = request.getParameter("userId");
        }

        return userId;
    }

    /**
     * Generates a unique span ID for this request
     */
    private String generateSpanId() {
        return generateUniqueId();
    }

    /**
     * Generates a unique identifier using UUID
     */
    private String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Clears all MDC values to prevent memory leaks
     */
    private void clearMDC() {
        MDC.remove(TRACE_ID_KEY);
        MDC.remove(SPAN_ID_KEY);
        MDC.remove(USER_ID_KEY);
        MDC.remove(REQUEST_URI_KEY);
        MDC.remove(REQUEST_METHOD_KEY);
    }

    /**
     * Static method to get the current trace ID from MDC
     * This is what RestClientExecutor.createLogObject() calls
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * Static method to get the current span ID from MDC
     */
    public static String getSpanId() {
        return MDC.get(SPAN_ID_KEY);
    }

    /**
     * Static method to get the current user ID from MDC
     */
    public static String getUserId() {
        return MDC.get(USER_ID_KEY);
    }

    /**
     * Static method to manually set a trace ID (useful for async processing)
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.hasText(traceId)) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    /**
     * Static method to manually clear trace context (useful for async processing)
     */
    public static void clearTraceContext() {
        MDC.clear();
    }

    /**
     * Gets current trace context as a string for logging
     */
    public static String getCurrentTraceContext() {
        String traceId = getTraceId();
        String spanId = getSpanId();
        String userId = getUserId();

        StringBuilder context = new StringBuilder();
        if (StringUtils.hasText(traceId)) {
            context.append("traceId=").append(traceId);
        }
        if (StringUtils.hasText(spanId)) {
            if (context.length() > 0) context.append(", ");
            context.append("spanId=").append(spanId);
        }
        if (StringUtils.hasText(userId)) {
            if (context.length() > 0) context.append(", ");
            context.append("userId=").append(userId);
        }

        return context.toString();
    }
}
