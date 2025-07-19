package com.netra.commons.requests.util;

import com.netra.commons.enums.TransactionParticipationRole;
import com.netra.commons.models.Transaction;
import com.netra.commons.models.TransactionParticipant;
import com.netra.commons.requests.CreateDisputeRequest;
import com.netra.commons.util.SecureHashingUtil;

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

        return SecureHashingUtil.sha256(canonical);
    }

}
