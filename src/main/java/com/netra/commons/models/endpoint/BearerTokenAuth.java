package com.netra.commons.models.endpoint;

import com.netra.commons.util.BasicUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@NoArgsConstructor
public class BearerTokenAuth implements AuthConfig {
    private final AuthType authType = AuthType.BEARER;
    private String token; // persisted vault ref for token


    // default "Authorization", but overrideable
    private String headerName = "Authorization";

    // default "Bearer", but overrideable (some APIs say "JWT" or "Token")
    private String prefix = "Bearer";

    public String buildValue(String secret) {

        return BasicUtil.validString(prefix)
                ? prefix + " " + secret
                : prefix;
    }
}

