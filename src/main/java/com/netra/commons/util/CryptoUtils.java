package com.netra.commons.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Locale;

public class CryptoUtils {

    public enum OutputFormat { BASE64, HEX }

    public static String sha256(String payload, String encoding) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = (payload == null) ? new byte[0] : payload.getBytes(StandardCharsets.UTF_8);
            byte[] digest = md.digest(bytes);
            return encodeBytes(digest, encoding);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }

    public static String encodeBytes(byte[] bytes, String encoding) {
        if ("HEX".equalsIgnoreCase(encoding)) {
            return bytesToHex(bytes);
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeBytes(byte[] bytes, OutputFormat format) {
        return switch (format) {
            case HEX -> bytesToHex(bytes);
            case BASE64 -> Base64.getEncoder().encodeToString(bytes);
        };
    }

    public static byte[] decodeString(String str, OutputFormat format) {
        return switch (format) {
            case HEX -> hexStringToByteArray(str);
            case BASE64 -> Base64.getDecoder().decode(str);
        };
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len % 2 != 0) throw new IllegalArgumentException("Hex string length must be even");
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
