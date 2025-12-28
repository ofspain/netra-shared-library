package com.netra.commons.models;

import com.netra.commons.enums.DisputeState;

import java.time.LocalDateTime;

public class DisputeJourneyTrace extends BaseEntity{// implements BlockchainAuditable {
    private String disputeId;
    private DisputeState fromState;
    private DisputeState toState;
    private LocalDateTime transitionTime;

    private String initiatedBy;       // e.g. ISSUER, ACQUIRER, SYSTEM
    private String initiatedByDomainCode;
    private String initiatedByUUID;
    private String initiatedByDomainType;

    private String reason;            // e.g. "Issuer Verified Funds"
    private String applicationChannel;// e.g. API, PORTAL

//    private String currentHash;       // local chain hash
//    private String previousHash;
//    private String auditTrace;        // can store serialized JSON of the trace
//
//    private String digitalSignature;  // optional signature
//    private String aptosTxnHash;      // <-- NEW: on-chain transaction reference
//    private String aptosEventRef;     // <-- NEW: event or proof reference
//
//    public String getAptosEventRef() {
//        return aptosEventRef;
//    }
//
//    public String getAptosTxnHash(){
//        return aptosTxnHash;
//    }
//
//    public void setAptosEventRef(String aptosEventRef) {
//        this.aptosEventRef = aptosEventRef;
//    }
//
//    public void setAptosTxnHash(String aptosTxnHash) {
//        this.aptosTxnHash = aptosTxnHash;
//    }

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

//    @Override
//    public String getCurrentHash() {
//        return currentHash;
//    }
//
//    public void setCurrentHash(String currentHash) {
//        this.currentHash = currentHash;
//    }
//
//    @Override
//    public String getPreviousHash() {
//        return previousHash;
//    }
//
//    public void setPreviousHash(String previousHash) {
//        this.previousHash = previousHash;
//    }
//
//    @Override
//    public String getAuditTrace() {
//        return auditTrace;
//    }
//
//    public void setAuditTrace(String auditTrace) {
//        this.auditTrace = auditTrace;
//    }
//
//    @Override
//    public String getDigitalSignature() {
//        return digitalSignature;
//    }
//
//    public void setDigitalSignature(String digitalSignature) {
//        this.digitalSignature = digitalSignature;
//    }

    public String getInitiatedByDomainCode() {
        return initiatedByDomainCode;
    }

    public void setInitiatedByDomainCode(String initiatedByDomainCode) {
        this.initiatedByDomainCode = initiatedByDomainCode;
    }

    public String getInitiatedByUUID() {
        return initiatedByUUID;
    }

    public void setInitiatedByUUID(String initiatedByUUID) {
        this.initiatedByUUID = initiatedByUUID;
    }

    public void setInitiatedByDomainType(String initiatedByDomainType) {
        this.initiatedByDomainType = initiatedByDomainType;
    }

    public String getInitiatedByDomainType() {
        return initiatedByDomainType;
    }

    //  private String digitalSignature; // Optional - for cryptographic proof
}
