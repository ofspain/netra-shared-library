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
    private String initiatedByCode; // issuer, system, acquirer, regulator
    private Long initiatedById; // issuer, system, acquirer, regulator

    private String reason;       // Optional freeform or enum (e.g. "issuer verified debit")
    private String applicationChannel;      // e.g., API, AdminConsole, DisputantWEDPortal, DisputantMobilePortal

    private String currentHash;
    private String previousHash;

    private String auditTrace;

    private String digitalSignature;

    public String getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        this.disputeId = disputeId;
    }

    public DisputeState getFromState() {
        return fromState;
    }

    public void setFromState(DisputeState fromState) {
        this.fromState = fromState;
    }

    public DisputeState getToState() {
        return toState;
    }

    public void setToState(DisputeState toState) {
        this.toState = toState;
    }

    public LocalDateTime getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(LocalDateTime transitionTime) {
        this.transitionTime = transitionTime;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplicationChannel() {
        return applicationChannel;
    }

    public void setApplicationChannel(String applicationChannel) {
        this.applicationChannel = applicationChannel;
    }

    @Override
    public String getCurrentHash() {
        return currentHash;
    }

    public void setCurrentHash(String currentHash) {
        this.currentHash = currentHash;
    }

    @Override
    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    @Override
    public String getAuditTrace() {
        return auditTrace;
    }

    public void setAuditTrace(String auditTrace) {
        this.auditTrace = auditTrace;
    }

    @Override
    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getInitiatedByCode() {
        return initiatedByCode;
    }

    public void setInitiatedByCode(String initiatedByCode) {
        this.initiatedByCode = initiatedByCode;
    }

    public Long getInitiatedById() {
        return initiatedById;
    }

    public void setInitiatedById(Long initiatedById) {
        this.initiatedById = initiatedById;
    }

//  private String digitalSignature; // Optional - for cryptographic proof
}
