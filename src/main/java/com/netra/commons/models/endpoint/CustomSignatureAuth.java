package com.netra.commons.models.endpoint;

import com.netra.commons.util.CryptoUtils;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * CustomSignatureAuth: configuration + canonical string + signing.
 *
 * - Only handles SIGNING & CANONICALIZATION.
 * - Encryption (AES/JWE/etc) must be handled by EncryptionConfig.
 */
@Data
public class CustomSignatureAuth implements AuthConfig {

    private final AuthConfig.AuthType authType = AuthType.CUSTOM_SIGNATURE;

    // Signing-related
    private String algo;                     // e.g. "HMAC-SHA256", "RSA-SHA256"
    private String key;                      // vault alias for signing key
    private SignaturePlacement placement = SignaturePlacement.HEADER;
    private String signatureName = "X-Signature";
    private List<SignatureComponent> components = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();

    /*
    About algo
    This is for digital signature algorithms / MACs.

    Examples:

        HMAC-SHA256 → turns into HmacSHA256 for Mac.getInstance(...).

        RSA-SHA256 → turns into SHA256withRSA for Signature.getInstance(...).

    Used to sign canonical string for integrity/authenticity.
     */

    public enum SignaturePlacement {
        HEADER,
        QUERY_PARAM
    }

    public enum SignatureComponent {
        METHOD,       // HTTP method
        PATH,         // request path
        QUERY_STRING, // raw query string (sorted & encoded)
        HEADERS,      // canonicalized headers
        BODY,         // body content (raw or hashed)
        TIMESTAMP,    // parameter-driven timestamp
        NONCE         // parameter-driven nonce
    }

    public enum BodyRepresentation {
        RAW,
        HASH // hashed with SHA-256 then base64/hex encoded
    }

    private BodyRepresentation bodyRepresentation = BodyRepresentation.RAW;

    // Build canonical string
    public String buildCanonicalString(String method,
                                       String path,
                                       String queryString,
                                       String body,
                                       Map<String, String> headersMap) {
        StringBuilder sb = new StringBuilder();

        for (SignatureComponent comp : components) {
            switch (comp) {
                case METHOD -> sb.append(nullSafe(method)).append("\n");
                case PATH -> sb.append(nullSafe(path)).append("\n");
                case QUERY_STRING -> sb.append(nullSafe(queryString)).append("\n");
                case BODY -> {
                    if (bodyRepresentation == BodyRepresentation.HASH) {
                        String hash = CryptoUtils.sha256(body,
                                parameters.getOrDefault("bodyHashEncoding", "BASE64"));
                        sb.append(hash).append("\n");
                    } else {
                        sb.append(nullSafe(body)).append("\n");
                    }
                }
                case HEADERS -> {
                    if (headersMap != null && !headersMap.isEmpty()) {
                        headersMap.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                                .forEach(e -> sb.append(e.getKey())
                                        .append(":")
                                        .append(nullSafe(e.getValue()))
                                        .append("\n"));
                    }
                }
                case TIMESTAMP -> {
                    String ts = parameters.getOrDefault("timestamp",
                            String.valueOf(System.currentTimeMillis()));
                    sb.append(ts).append("\n");
                }
                case NONCE -> {
                    String nonce = parameters.getOrDefault("nonce", UUID.randomUUID().toString());
                    sb.append(nonce).append("\n");
                }
            }
        }

        return sb.toString().trim();
    }

    // Sign the canonical string
    public String sign(String canonical, String signingSecret) {
        if (canonical == null) canonical = "";

        try {
            String encoding = parameters.getOrDefault("signatureEncoding", "BASE64").toUpperCase(Locale.ROOT);

            if (algo == null) throw new IllegalStateException("Signing algorithm not set");

            if (algo.toUpperCase().startsWith("HMAC")) {
                String macName = normalizeHmacAlgo(algo);
                Mac mac = Mac.getInstance(macName);
                SecretKeySpec keySpec = new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), macName);
                mac.init(keySpec);
                byte[] raw = mac.doFinal(canonical.getBytes(StandardCharsets.UTF_8));
                return CryptoUtils.encodeBytes(raw, encoding);
            } else if (algo.toUpperCase().contains("RSA")) {
                String sigName = normalizeRsaAlgo(algo);
                Signature signature = Signature.getInstance(sigName);
                PrivateKey privateKey = loadPrivateKeyFromBase64(signingSecret);
                signature.initSign(privateKey);
                signature.update(canonical.getBytes(StandardCharsets.UTF_8));
                byte[] raw = signature.sign();
                return CryptoUtils.encodeBytes(raw, encoding);
            } else {
                throw new IllegalArgumentException("Unsupported signing algorithm: " + algo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign canonical string", e);
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private String normalizeHmacAlgo(String algo) {
        return algo.replace("-", "").replace("HMAC", "Hmac"); // HMAC-SHA256 -> HmacSHA256
    }

    private String normalizeRsaAlgo(String algo) {
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
}
