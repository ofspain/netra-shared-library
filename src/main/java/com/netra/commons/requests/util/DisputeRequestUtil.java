package com.netra.commons.requests.util;

import com.netra.commons.enums.TransactionParticipationRole;
import com.netra.commons.models.Transaction;
import com.netra.commons.requests.CreateDisputeRequest;
import com.netra.commons.util.SecureHashingUtil;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DisputeRequestUtil {
    public static String generateDisputeKey(CreateDisputeRequest request) {
        String issuer = request.getAffectedAccount().getIssuingInstitution().getCode();

        String canonical = String.join("|",
                request.getIssuerTransactionRef(),
                request.getTransactionDate().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                String.valueOf(request.getTransactionAmount()),
                request.getTransactionRail().getInstrument().name(),
                request.getTransactionRail().getPaymentRail().name(),
                request.getTransactionAction().name(),
                issuer
        );

        return SecureHashingUtil.sha256(canonical);
    }

}
