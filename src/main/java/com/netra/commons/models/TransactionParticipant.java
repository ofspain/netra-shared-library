package com.netra.commons.models;

import com.netra.commons.contracts.Domain;
import com.netra.commons.enums.TransactionParticipationRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionParticipant extends BaseEntity {
    private Domain participant;

    //must be present for issuer
    private AccountDetail accountDetail;
    private LocalDateTime notifiedAt;

    private LocalDateTime acknowledgeAt;

    private TransactionParticipationRole transactionParticipationRole;

}
