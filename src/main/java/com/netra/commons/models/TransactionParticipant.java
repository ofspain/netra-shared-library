package com.netra.commons.models;

import com.netra.commons.contracts.Domain;
import com.netra.commons.contracts.enums.TransactionParticipationRole;

import java.time.LocalDateTime;

public class TransactionParticipant {
    private Domain participant;

    //must be present for issuer
    private AccountDetail accountDetail;
    private LocalDateTime notifiedAt;

    private LocalDateTime acknowledgeAt;

    private TransactionParticipationRole transactionParticipationRole;

}
