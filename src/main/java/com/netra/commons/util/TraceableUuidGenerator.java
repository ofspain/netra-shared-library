package com.netra.commons.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TraceableUuidGenerator {

    // Short hash or use domainCode directly if it's short
    private static String getDomainPrefix(String domainCode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(domainCode.getBytes(StandardCharsets.UTF_8));

            // Take first 4 bytes and hex encode as prefix (8 chars)
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString(); // e.g. "a1b2c3d4"
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    public static String generateTraceableUuid(String domainCode) {
        String domainPrefix = getDomainPrefix(domainCode);
        UUID randomPart = UUID.randomUUID();
        return domainPrefix + "-" + randomPart.toString();
    }

    public static String extractDomainPrefix(String blendedId) {
        if (blendedId == null || !blendedId.contains("-")) {
            throw new IllegalArgumentException("Invalid blended ID format");
        }

        return blendedId.substring(0, blendedId.indexOf("-"));
    }

    public static UUID extractUuid(String blendedId) {
        if (blendedId == null || !blendedId.contains("-")) {
            throw new IllegalArgumentException("Invalid blended ID format");
        }

        String uuidPart = blendedId.substring(blendedId.indexOf("-") + 1);
        return UUID.fromString(uuidPart);
    }

}

