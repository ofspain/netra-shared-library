package com.netra.commons.external.service.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;

/**
 * LogObject class for capturing REST API call details for logging and monitoring purposes.
 * This class holds all the necessary information about HTTP requests and responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogObject {

    /**
     * Unique trace ID for correlating logs across services
     */
    private String traceID;

    /**
     * Username or identifier of the user making the request
     */
    private String user;

    /**
     * Name of the calling service/module
     */
    private String store;

    /**
     * Type of operation being performed (e.g., CREATE, READ, UPDATE, DELETE)
     */
    private String operation;

    /**
     * The endpoint URL being called
     */
    private String endpoint;

    /**
     * HTTP method (GET, POST, PUT, DELETE, etc.)
     */
    private String httpMethod;

    /**
     * Serialized request body
     */
    private String request;

    /**
     * Serialized response body
     */
    private String storeResponse;

    /**
     * HTTP headers sent with the request
     */
    private HttpHeaders httpHeader;

    /**
     * Whether the request was successful
     */
    private Boolean isSuccessful;

    /**
     * HTTP status code returned
     */
    private String statusCode;

    /**
     * Time taken for the request to complete
     */
    private String timeTaken;

    /**
     * Timestamp when the request was initiated
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Additional error message if the request failed
     */
    private String errorMessage;

    /**
     * Request size in bytes (optional)
     */
    private Long requestSize;

    /**
     * Response size in bytes (optional)
     */
    private Long responseSize;

    /**
     * Client IP address (optional)
     */
    private String clientIp;

    /**
     * User agent information (optional)
     */
    private String userAgent;

    /**
     * Session ID if applicable (optional)
     */
    private String sessionId;

    /**
     * API version being called (optional)
     */
    private String apiVersion;

    /**
     * Environment where the call was made (DEV, TEST, PROD)
     */
    private String environment;

    /**
     * Correlation ID for distributed tracing (optional)
     */
    private String correlationId;

    /**
     * Additional metadata as key-value pairs (optional)
     */
    private java.util.Map<String, Object> metadata;

    /**
     * Convenience method to set success status with status code
     */
    public void setSuccess(boolean success, String statusCode) {
        this.isSuccessful = success;
        this.statusCode = statusCode;
    }

    /**
     * Convenience method to set failure status with error details
     */
    public void setFailure(String statusCode, String errorMessage) {
        this.isSuccessful = false;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    /**
     * Check if the request was successful
     */
    public boolean isSuccess() {
        return Boolean.TRUE.equals(isSuccessful);
    }

    /**
     * Get a summary string for quick logging
     */
    public String getSummary() {
        return String.format("[%s] %s %s - %s (%s) - %s",
                traceID != null ? traceID : "NO_TRACE",
                httpMethod != null ? httpMethod : "UNKNOWN",
                endpoint != null ? endpoint : "NO_URL",
                statusCode != null ? statusCode : "NO_STATUS",
                timeTaken != null ? timeTaken : "NO_TIME",
                Boolean.TRUE.equals(isSuccessful) ? "SUCCESS" : "FAILED"
        );
    }
}
