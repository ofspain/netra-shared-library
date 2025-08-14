package com.netra.commons.util;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@UtilityClass
public class BasicUtil {

    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public Boolean validString(String string){
       return  string != null && !string.isBlank();
    }


    /**
     * Generates a short alphanumeric string.
     */
    public String generateRandomAlphanumeric(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a checksum (first 2 chars of SHA-1 hash of input).
     */
    public String generateChecksum(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b).toUpperCase();
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 2);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }

    /**
     * Generates a unique global agent ID.
     * @param institutionCode - Short code for the institution (e.g., "FBNG").
     * @return The generated global agent ID.
     */
    public String generateGlobalAccessPointId(String institutionCode) {
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
        final int RANDOM_SEGMENT_LENGTH = 6;

        String datePart = LocalDate.now().format(DATE_FORMATTER);
        String randomPart = BasicUtil.generateRandomAlphanumeric(RANDOM_SEGMENT_LENGTH);
        String rawId = institutionCode.toUpperCase() + "-" + datePart + "-" + randomPart;

        String checksum = BasicUtil.generateChecksum(rawId);
        return rawId + "-" + checksum;
    }

    /**
     * Breaks down a global entity ID into its components.
     * @param globalAccessPointId - The global agent ID to decompose.
     * @return Map with institutionCode, date, randomPart, checksum.
     */
    public Map<String, String> decomposeGlobalAccessPointId(String globalAccessPointId) {
        String[] parts = globalAccessPointId.split("-");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid Global Entity ID format: " + globalAccessPointId);
        }

        Map<String, String> components = new HashMap<>();
        components.put("institutionCode", parts[0]);
        components.put("date", parts[1]);        // YYMMDD format
        components.put("randomPart", parts[2]);
        components.put("checksum", parts[3]);

        return components;
    }
}
