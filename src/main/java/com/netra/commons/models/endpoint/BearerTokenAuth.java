package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BearerTokenAuth implements AuthConfig {
    private final AuthType authType = AuthType.BEARER;
    private String token; // persisted vault ref for token


    // default "Authorization", but overrideable
    private String headerName = "Authorization";

    // default "Bearer", but overrideable (some APIs say "JWT" or "Token")
    private String prefix = "Bearer";
}

