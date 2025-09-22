package com.netra.commons.models.endpoint;

import com.netra.commons.util.BasicUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@NoArgsConstructor
public class BasicAuth implements AuthConfig {
    private final AuthType authType = AuthType.BASIC;

    private String username;                 // optional
    private String password;       // vault alias (never raw)
    private String headerName = "Authorization"; // default, but overrideable

    private String tokenPrefix = "Basic";

    public String buildValue(String user, String pass) {
        String creds = Base64.getEncoder()
                .encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));

        return BasicUtil.validString(tokenPrefix)
                ? tokenPrefix + " " + creds
                : creds;
    }

}
