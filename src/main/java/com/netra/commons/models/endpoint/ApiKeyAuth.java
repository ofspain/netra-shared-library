package com.netra.commons.models.endpoint;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiKeyAuth implements AuthConfig {
    private final AuthType authType = AuthType.API_KEY;

    // Where to put the key
    private String headerName;         // e.g. "X-API-KEY" or "Authorization"
    private String queryParamName;     // if API key goes in URL
    private boolean asQueryParam = false;

    // Always a vault alias, never literal
    private String apiKey;

    // Decorations
    private String prefix;             // e.g. "Bearer", "ApiKey", "Token"
    private String prefixSeparator = " "; // default space ("Bearer <key>")

    private String suffix;             // e.g. "@uat"
    private String suffixSeparator = "";  // default glue (no space)
}
