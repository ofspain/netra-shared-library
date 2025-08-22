package com.netra.commons.models;

import com.netra.commons.enums.DomainType;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
public class EndpointConfig extends BaseEntity {

    // Basic identity
    private String domainCode;                     // e.g. "MONNIFY", "BANK_GTB"
    private DomainType domainType;                 // e.g. BANK, WALLET, FX_PROVIDER
    private String description;                    // e.g. Endpoint for monify

    // Base communication setup
    private String baseUrl;                        // e.g. https://api.example.com
    private int timeoutMillis = 5000;
    private boolean useProxy;
    private ProxyConfig proxy;

    // Auth
    private boolean requiresAuth;
    private AuthType authType = AuthType.NONE;

    // Unique transaction endpoint
    private EndpointDetail uniqueTransaction;

    // Bulk/multiple transaction endpoint
    private EndpointDetail multipleTransaction;

    private String requestBodyTemplate;

    // Resilience config
    private RetryConfig retryConfig;
    private FallbackConfig fallbackConfig;

    // Enums
    public enum AuthType {
        NONE, API_KEY, BEARER_TOKEN, BASIC, MTLS
    }


    public enum FallbackType {
        STATIC_RESPONSE, REDIRECT_ENDPOINT, EXCEPTION
    }


    // ========== Nested Classes ========== //

    @Data
    public static class RetryConfig {
        private int maxAttempts = 3;
        private long initialDelayMillis = 200;
        private double multiplier = 2.0;
        private long maxDelayMillis = 2000;
    }

    @Data
    public static class FallbackConfig {
        private FallbackType type;
        private Map<String, Object> value;
    }

    @Data
    @ToString
    public static class ProxyConfig {
        private String host;
        private Integer port;
        private String username;
        //todo: passwords are stored as encrypted value in db, and decrypted for use at runtime
        private String password;
        private ProxyType type = ProxyType.HTTP;
    }

    public enum ProxyType {
        HTTP, SOCKS
    }

    public enum HTTPMethod {
        GET, POST // Only for read-type access
    }

    @Data
    @ToString
    public static class EndpointDetail {
        private String url;
        private HTTPMethod method;
        private List<String> pathParamKeys;    // Names only
        private List<String> queryParamKeys;
        private List<EndpointHeader> headers;
    }

    @Data
    @ToString
    public static class EndpointHeader {
        private String name;
        private String value;
        private boolean dynamic;

        public EndpointHeader(){}

        public EndpointHeader(String name, String value, boolean dynamic){
            this.name = name;
            this.value = value;
            this.dynamic = dynamic;
        }
    }

}

/**
 * 📐 A schema DDL to create the table
 * *
 *
 CREATE TABLE endpoint_config (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

 -- Basic identity
 domain_code VARCHAR(100) NOT NULL UNIQUE,
 domain_type VARCHAR(50) NOT NULL,
 description TEXT,

 -- Core comm setup
 base_url TEXT NOT NULL,
 timeout_millis INT NOT NULL DEFAULT 5000,
 use_proxy BOOLEAN NOT NULL DEFAULT false,

 -- Proxy
 proxy_config JSONB,

 -- Auth
 requires_auth BOOLEAN NOT NULL DEFAULT false,
 auth_type VARCHAR(50) NOT NULL DEFAULT 'NONE',

 -- Request template
 request_body_template TEXT,

 -- Endpoint detail
 unique_transaction JSONB,
 multiple_transaction JSONB,

 -- Resilience registry
 retry_config JSONB,
 fallback_config JSONB,

 -- Metadata
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 );

 *
 * CREATE INDEX idx_endpoint_config_domain_code ON endpoint_config(domain_code);
 * CREATE INDEX idx_endpoint_config_domain_type ON endpoint_config(domain_type);
 * CREATE INDEX idx_endpoint_config_auth_type ON endpoint_config(auth_type);
 *
 * EASY JSON LOOKUP
 *
 * -- For fast lookup by proxy type, auth header name, etc.
 * CREATE INDEX idx_endpoint_config_proxy_json ON endpoint_config USING GIN (proxy);
 * CREATE INDEX idx_endpoint_config_unique_txn_json ON endpoint_config USING GIN (unique_transaction);
 *
 *
 *
 * ✅ Java model ↔ DB (serialization logic)
 *
 * ✅ Sample insert via Spring or SQL
 *
 * ✅ JSONB validation at persistence layer
 *
 * ✅ DDL for a smaller table just to store reusable headers (if needed)
 */
