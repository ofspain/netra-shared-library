package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CustomSignatureAuth implements AuthConfig {
    private final AuthType authType = AuthType.CUSTOM_SIGNATURE;
    private String algo;
    private String key;
    private Map<String, String> parameters;
}