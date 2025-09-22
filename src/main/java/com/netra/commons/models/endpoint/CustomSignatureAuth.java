package com.netra.commons.models.endpoint;

import lombok.Data;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * CustomSignatureAuth: configuration + signing + optional AES-GCM encrypt/decrypt helpers.
 *
 * NOTES:
 * - This class does NOT fetch secrets from vault; caller must pass resolved secrets to methods.
 * - AES-GCM implementation uses JCE ("AES/GCM/NoPadding"). Tag length default = 128 bits.
 * - For deterministic canonical strings, query strings and header order are sorted by key.
 */

@Data
public class CustomSignatureAuth implements AuthConfig {

    private final AuthConfig.AuthType authType = AuthType.CUSTOM_SIGNATURE;

    // Signing-related
    private String algo;                     // e.g. "HMAC-SHA256" or "RSA-SHA256"
    private String key;                      // vault alias for signing key (not secret itself)
    private SignaturePlacement placement = SignaturePlacement.HEADER;
    private String signatureName = "X-Signature";
    private List<SignatureComponent> components = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();

    // Optional encryption configuration (if you want to encrypt request bodies)
    // encryptionKeyAlias is separate from signing key alias (if used)
    private boolean encryptionEnabled = false;
    private String encryptionKey;       // vault alias for AES key (base64 encoded 256-bit key expected)
    private EncryptionOutputFormat encryptionOutputFormat = EncryptionOutputFormat.BASE64; // result format
    private boolean signEncryptedBody = true; // whether canonical uses encrypted body (true) or plain body (false)
    private BodyRepresentation bodyRepresentation = BodyRepresentation.RAW; // RAW or HASH

    // -------------------- nested enums --------------------
    public enum SignaturePlacement {
        HEADER,
        QUERY_PARAM
    }

    public enum SignatureComponent {
        METHOD,       // HTTP method
        PATH,         // request path
        QUERY_STRING, // raw query string (sorted/encoded)
        HEADERS,      // canonicalized headers
        BODY,         // body content (raw or hashed - controlled)
        TIMESTAMP,    // parameter-driven timestamp
        NONCE         // parameter-driven nonce
    }

    public enum EncryptionOutputFormat {
        BASE64,
        HEX
    }

    public enum BodyRepresentation {
        RAW,    // include raw body text
        HASH    // include hash (SHA-256 hex/base64) of the body
    }

    // ------------------ canonical string builder ------------------

    /**
     * Build canonical string based on configured components.
     *
     * @param method    HTTP method, e.g., "POST"
     * @param path      request path, e.g., "/api/v1/orders"
     * @param queryString  canonical query string (sorted & encoded)
     * @param body      body to include (either plain or encrypted depending on caller)
     * @param headersMap map of headers (single-value)
     * @return canonical string (trimmed)
     */
    public String buildCanonicalString(String method,
                                       String path,
                                       String queryString,
                                       String body,
                                       Map<String, String> headersMap) {

        StringBuilder sb = new StringBuilder();

        for (SignatureComponent comp : components) {
            switch (comp) {
                case METHOD:
                    sb.append(nullSafe(method)).append("\n");
                    break;
                case PATH:
                    sb.append(nullSafe(path)).append("\n");
                    break;
                case QUERY_STRING:
                    sb.append(nullSafe(queryString)).append("\n");
                    break;
                case BODY:
                    if (bodyRepresentation == BodyRepresentation.HASH) {
                        String hash = sha256Base64OrHex(body); // default base64; parameters can specify "bodyHashEncoding"
                        sb.append(hash).append("\n");
                    } else {
                        sb.append(nullSafe(body)).append("\n");
                    }
                    break;
                case HEADERS:
                    if (headersMap != null && !headersMap.isEmpty()) {
                        // sort case-insensitively
                        headersMap.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                                .forEach(e -> sb.append(e.getKey()).append(":").append(nullSafe(e.getValue())).append("\n"));
                    }
                    break;
                case TIMESTAMP:
                    String ts = parameters.getOrDefault("timestamp", String.valueOf(System.currentTimeMillis()));
                    sb.append(ts).append("\n");
                    break;
                case NONCE:
                    String nonce = parameters.getOrDefault("nonce", UUID.randomUUID().toString());
                    sb.append(nonce).append("\n");
                    break;
            }
        }

        return sb.toString().trim();
    }

    // ------------------ signing ------------------

    /**
     * Generate signature for a canonical string using signingSecret (the actual secret bytes or private key material).
     * For HMAC algos: signingSecret is the raw secret (string form) used as HMAC key.
     * For RSA algos: signingSecret is base64-encoded PKCS#8 private key bytes.
     *
     * @param canonical the string to sign
     * @param signingSecret the resolved secret (caller fetched from vault)
     * @return Base64-encoded signature by default (unless parameters specify "signatureEncoding" = "HEX")
     */
    public String sign(String canonical, String signingSecret) {
        if (canonical == null) canonical = "";

        try {
            String encoding = parameters.getOrDefault("signatureEncoding", "BASE64").toUpperCase(Locale.ROOT);

            if (algo == null) throw new IllegalStateException("Signing algorithm not set");

            if (algo.toUpperCase().startsWith("HMAC")) {
                String macName = normalizeHmacAlgo(algo); // e.g. "HmacSHA256"
                Mac mac = Mac.getInstance(macName);
                SecretKeySpec keySpec = new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), macName);
                mac.init(keySpec);
                byte[] raw = mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8));
                return encodeBytes(raw, encoding);
            } else if (algo.toUpperCase().contains("RSA")) {
                String sigName = normalizeRsaAlgo(algo); // e.g. "SHA256withRSA"
                Signature signature = Signature.getInstance(sigName);
                PrivateKey privateKey = loadPrivateKeyFromBase64(signingSecret); // signingSecret expected base64 PKCS#8
                signature.initSign(privateKey);
                signature.update(canonical.getBytes(StandardCharsets.UTF_8));
                byte[] raw = signature.sign();
                return encodeBytes(raw, encoding);
            } else {
                throw new IllegalArgumentException("Unsupported signing algorithm: " + algo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign canonical string", e);
        }
    }

    // ------------------ optional AES-GCM encryption/decryption helpers ------------------

    /**
     * Encrypt a plaintext body using AES-GCM.
     * - encryptionKeyBase64: base64 encoded symmetric key (expected 16/24/32 bytes).
     * - Returns an EncryptedPayload containing iv (base64), ciphertext (base64) and combined (iv:cipher) string.
     *
     * Caller decides how to place the ciphertext in the request (body or wrapper).
     */
    public EncryptedPayload encryptBody(String plainBody, String encryptionKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
            byte[] iv = new byte[12]; // 96-bit recommended for GCM
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // tag length 128 bits
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] plainBytes = (plainBody == null) ? new byte[0] : plainBody.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(plainBytes);

            String ivB64 = Base64.getEncoder().encodeToString(iv);
            String ctB64 = Base64.getEncoder().encodeToString(encrypted);

            if (encryptionOutputFormat == EncryptionOutputFormat.HEX) {
                String ivHex = bytesToHex(iv);
                String ctHex = bytesToHex(encrypted);
                return new EncryptedPayload(ivHex, ctHex, ivHex + ":" + ctHex, EncryptionOutputFormat.HEX);
            } else {
                return new EncryptedPayload(ivB64, ctB64, ivB64 + ":" + ctB64, EncryptionOutputFormat.BASE64);
            }

        } catch (Exception e) {
            throw new RuntimeException("AES-GCM encryption failed", e);
        }
    }

    /**
     * Decrypt AES-GCM ciphertext. Provide iv and ciphertext in same format as encryptionOutputFormat.
     *
     * @param cipherTextStr encrypted payload (base64 or hex)
     * @param ivStr iv (base64 or hex)
     * @param encryptionKeyBase64 base64-encoded AES key (the same used to encrypt)
     * @return plaintext string
     */
    public String decryptBody(String cipherTextStr, String ivStr, String encryptionKeyBase64) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encryptionKeyBase64);
            byte[] ct;
            byte[] iv;

            if (encryptionOutputFormat == EncryptionOutputFormat.HEX) {
                ct = hexStringToByteArray(cipherTextStr);
                iv = hexStringToByteArray(ivStr);
            } else {
                ct = Base64.getDecoder().decode(cipherTextStr);
                iv = Base64.getDecoder().decode(ivStr);
            }

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

    // ------------------ helpers ------------------

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private String sha256Base64OrHex(String payload) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = (payload == null) ? new byte[0] : payload.getBytes(StandardCharsets.UTF_8);
            byte[] digest = md.digest(bytes);
            String preferred = parameters.getOrDefault("bodyHashEncoding", "BASE64").toUpperCase(Locale.ROOT);
            return encodeBytes(digest, preferred);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String normalizeHmacAlgo(String algo) {
        // maps "HMAC-SHA256" or "HMAC-SHA1" -> "HmacSHA256", etc.
        String a = algo.replace("-", "").replace("HMAC", "Hmac");
        // if already in form HmacSHA256 this will not break
        return a;
    }

    private String normalizeRsaAlgo(String algo) {
        // maps common forms
        if ("RSA-SHA256".equalsIgnoreCase(algo) || "SHA256withRSA".equalsIgnoreCase(algo)) return "SHA256withRSA";
        if ("RSA-SHA1".equalsIgnoreCase(algo) || "SHA1withRSA".equalsIgnoreCase(algo)) return "SHA1withRSA";
        return algo;
    }

    private PrivateKey loadPrivateKeyFromBase64(String base64Pkcs8) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Pkcs8);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private String encodeBytes(byte[] bytes, String encoding) {
        if ("HEX".equalsIgnoreCase(encoding)) {
            return bytesToHex(bytes);
        } else {
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) throw new IllegalArgumentException("Hex string length must be even");
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // container for encryption result
    public static class EncryptedPayload {
        private final String iv;         // iv (base64 or hex depending on output format)
        private final String ciphertext; // ciphertext (base64 or hex)
        private final String combined;   // iv:ciphertext (string)
        private final EncryptionOutputFormat format;

        public EncryptedPayload(String iv, String ciphertext, String combined, EncryptionOutputFormat format) {
            this.iv = iv;
            this.ciphertext = ciphertext;
            this.combined = combined;
            this.format = format;
        }

        public String getIv() { return iv; }
        public String getCiphertext() { return ciphertext; }
        public String getCombined() { return combined; }
        public EncryptionOutputFormat getFormat() { return format; }
    }
}
