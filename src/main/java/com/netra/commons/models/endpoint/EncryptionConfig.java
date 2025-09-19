package com.netra.commons.models.endpoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class EncryptionConfig {
    private EncryptionType type = EncryptionType.NONE;
    private String algorithm;
    private String encryptionKey; // vault alias
    private String ivParam;
    private List<AadHeader> aadHeaders;
    private boolean signPayload = false;
    private String signatureKey;
    public enum EncryptionType { NONE, AES, JWE, CUSTOM }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AadHeader {
        private String name;          // always required
        private String value;         // optional: template, vault alias, static value, etc.
        private boolean isSecret;     // true → resolve from vault
        private boolean isDynamic;    // true → must be provided by caller at runtime
    }
}


