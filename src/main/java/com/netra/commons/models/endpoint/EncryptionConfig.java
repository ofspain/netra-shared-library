package com.netra.commons.models.endpoint;

import com.netra.commons.util.CryptoUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * EncryptionConfig: defines encryption strategy for an endpoint.
 * - Only handles ENCRYPTION/DECRYPTION.
 * - No signing/canonicalization logic here.
 */
@Data
@NoArgsConstructor
public class EncryptionConfig {

    private EncryptionType type = EncryptionType.NONE;
    private String algorithm;              // e.g. "AES/GCM/NoPadding"
    private String encryptionKey;          // vault alias
    private String ivParam;                // optional IV/nonce vault alias
    private java.util.List<AadHeader> aadHeaders;

    /** Optional: specifies whether request/response is BASE64 or HEX encoded */
    private CryptoUtils.OutputFormat outputFormat = CryptoUtils.OutputFormat.BASE64;


    /*
    About algorithm
    This is for cipher suites / encryption algorithms.

    Examples:

        AES/GCM/NoPadding

        AES/CBC/PKCS5Padding

        RSA/ECB/OAEPWithSHA-256AndMGF1Padding

    Used by Cipher.getInstance(algorithm) for encrypt/decrypt.
     */

    public enum EncryptionType { NONE, AES, JWE, CUSTOM }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AadHeader {
        private String name;
        private String value;
        private boolean isSecret;
        private boolean isDynamic;
    }

    /**
     * AES-GCM encryption helper.
     */
    public EncryptedPayload encryptBody(String plainBody, String encryptionKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
            byte[] iv = new byte[12]; // 96-bit recommended for GCM
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] plainBytes = (plainBody == null) ? new byte[0] : plainBody.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(plainBytes);

            return new EncryptedPayload(
                    CryptoUtils.encodeBytes(iv, this.outputFormat),
                    CryptoUtils.encodeBytes(encrypted, this.outputFormat),
                    this.outputFormat
            );
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM encryption failed", e);
        }
    }

    /**
     * AES-GCM decryption helper.
     */
    public String decryptBody(String cipherTextStr, String ivStr,
                              String encryptionKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
            byte[] ct = CryptoUtils.decodeString(cipherTextStr, this.outputFormat);
            byte[] iv = CryptoUtils.decodeString(ivStr, this.outputFormat);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] plain = cipher.doFinal(ct);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM decryption failed", e);
        }
    }

    @Data
    public static class EncryptedPayload {
        private final String iv;
        private final String ciphertext;
        private final CryptoUtils.OutputFormat format;

        public String getCombined() {
            return iv + ":" + ciphertext;
        }
    }
}
