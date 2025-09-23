package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class SecurityConfig {
    private List<AuthConfig> authConfigs = new ArrayList<>();
    private EncryptionConfig encryption;

    private boolean isEncryptPayload = false;
}
