package com.netra.commons.models;

import com.netra.commons.contracts.BlockchainAuditable;
import com.netra.commons.enums.DisputeState;

import java.time.LocalDateTime;

public class DisputeJourneyTrace extends BaseEntity implements BlockchainAuditable {
    private String disputeId;
    private DisputeState fromState;
    private DisputeState toState;
    private LocalDateTime transitionTime;
    private String initiatedBy; // issuer, system, acquirer, regulator

    private String reason;       // Optional freeform or enum (e.g. "issuer verified debit")
    private String applicationChannel;      // e.g., API, AdminConsole, DisputantWEDPortal, DisputantMobilePortal

    @Override
    public String getAuditTrace() {
        return null;
    }

    @Override
    public String getCurrentHash() {
        return null;
    }

    @Override
    public String getPreviousHash() {
        return null;
    }

    @Override
    public String getDigitalSignature() {
        return null;
    }


    //  private String digitalSignature; // Optional - for cryptographic proof
}
