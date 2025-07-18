package com.netra.commons.requests.util;

import com.netra.commons.enums.TransactionParticipationRole;
import com.netra.commons.models.Transaction;
import com.netra.commons.models.TransactionParticipant;
import com.netra.commons.requests.CreateDisputeRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisputeRequestUtil {
    public static String generateDisputeKey(CreateDisputeRequest request) {
        Transaction tx = request.getTransaction();
        List<TransactionParticipant> participants = request.getParticipants();

        String issuer = participants.stream()
                .filter(p -> p.getTransactionParticipationRole().equals(TransactionParticipationRole.ISSUER))
                .map(p -> p.getParticipant().getCode())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Issuer must be provided"));

        String canonical = String.join("|",
                tx.getTransactionRef(),
                tx.getTransactionDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                String.valueOf(tx.getAmount()),
                tx.getTransactionRailDTO().getInstrument().name(),
                tx.getTransactionRailDTO().getChannel().name(),
                tx.getTransactionType().getName(),
                issuer
        );

        return sha256(canonical);
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

}
