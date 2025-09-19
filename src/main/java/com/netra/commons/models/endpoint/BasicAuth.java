package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicAuth implements AuthConfig {
    private final AuthType authType = AuthType.BASIC;

    private String username;                 // optional
    private String password;       // vault alias (never raw)
    private String headerName = "Authorization"; // default, but overrideable
}
