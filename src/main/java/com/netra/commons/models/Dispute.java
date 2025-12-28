package com.netra.commons.models;

import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Dispute extends BaseEntity {

        private String logCode; // generate on DB
        private OtherTransactionInfo otherTransactionInfo;

        private LocalDateTime disputeMarkedLegitTime; // applicable only for user-raised dispute, marked when issuer verified
        private DisputeState currentState;
        private DisputeState previousState;

        private LocalDateTime transactionDate;
        private BigDecimal transactionAmount;

        private TransactionAction transactionAction;
        private PaymentRail transactionPaymentRail;
        private TransactionInstrument  transactionInstrument;

        private ApplicationChannel createdVia;
        private Disputant createdBy;
        private String note;

        private String issuerCode;
        private String acquirerCode;
        private String merchantCode;
        private String beneficiaryCode;
        private String switcherCode;

        private String billerCode;

        private DisputeMode disputeMode;
        private boolean locked;

        private List<Evidence> evidences = new ArrayList<>();
        private List<DisputeJournal> disputeJournals = new ArrayList<>();

        /** Resolution flags */
        private boolean isFinalized;             // reached any end state
        private boolean isResolved;              // reached a definite resolution
        private boolean resolvedInCustomerFavor; // outcome flag

        /* ---------------------- NEW FIELDS ---------------------- */

        /** Institution initiating the dispute */
        private String plaintiffInstitutionCode;

        /** Institution the dispute is raised against */
        private String defendantInstitutionCode;

        /** Indicates an intra-institutional dispute (issuer == acquirer or plaintiff == defendant) */
        private boolean onUsTransaction;

        /* ---------------------- GETTERS/SETTERS ---------------------- */

        public OtherTransactionInfo getTransaction() { return otherTransactionInfo; }
        public void setTransaction(OtherTransactionInfo otherTransactionInfo) { this.otherTransactionInfo = otherTransactionInfo; }

        public LocalDateTime getDisputeMarkedLegitTime() { return disputeMarkedLegitTime; }
        public void setDisputeMarkedLegitTime(LocalDateTime disputeMarkedLegitTime) {
                this.disputeMarkedLegitTime = disputeMarkedLegitTime;
        }

        public DisputeState getCurrentState() { return currentState; }
        public void setCurrentState(DisputeState currentState) { this.currentState = currentState; }

        public DisputeState getPreviousState() { return previousState; }
        public void setPreviousState(DisputeState previousState) { this.previousState = previousState; }

        public ApplicationChannel getCreatedVia() { return createdVia; }
        public void setCreatedVia(ApplicationChannel createdVia) { this.createdVia = createdVia; }

        public Disputant getCreatedBy() { return createdBy; }
        public void setCreatedBy(Disputant createdBy) { this.createdBy = createdBy; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }

        public String getIssuerCode() { return issuerCode; }
        public void setIssuerCode(String issuerCode) { this.issuerCode = issuerCode; }

        public String getAcquirerCode() { return acquirerCode; }
        public void setAcquirerCode(String acquirerCode) { this.acquirerCode = acquirerCode; }

        public String getMerchantCode() { return merchantCode; }
        public void setMerchantCode(String merchantCode) { this.merchantCode = merchantCode; }

        public String getBeneficiaryCode() { return beneficiaryCode; }
        public void setBeneficiaryCode(String beneficiaryCode) { this.beneficiaryCode = beneficiaryCode; }

        public String getSwitcherCode() { return switcherCode; }
        public void setSwitcherCode(String switcherCode) { this.switcherCode = switcherCode; }

        public DisputeMode getDisputeMode() { return disputeMode; }
        public void setDisputeMode(DisputeMode disputeMode) { this.disputeMode = disputeMode; }

        public boolean isLocked() { return locked; }
        public void setLocked(boolean locked) { this.locked = locked; }

        public List<Evidence> getEvidences() { return evidences; }
        public void setEvidences(List<Evidence> evidences) { this.evidences = evidences; }

        public List<DisputeJournal> getDisputeJournals() { return disputeJournals; }
        public void setDisputeJournals(List<DisputeJournal> disputeJournals) {
                this.disputeJournals = disputeJournals;
        }

        public boolean isFinalized() { return isFinalized; }
        public void setFinalized(boolean finalized) { isFinalized = finalized; }

        public boolean isResolved() { return isResolved; }
        public void setResolved(boolean resolved) { isResolved = resolved; }

        public boolean isResolvedInCustomerFavor() { return resolvedInCustomerFavor; }
        public void setResolvedInCustomerFavor(boolean resolvedInCustomerFavor) {
                this.resolvedInCustomerFavor = resolvedInCustomerFavor;
        }

        public String getLogCode() { return logCode; }
        public void setLogCode(String logCode) { this.logCode = logCode; }

        public String getPlaintiffInstitutionCode() { return plaintiffInstitutionCode; }
        public void setPlaintiffInstitutionCode(String plaintiffInstitutionCode) {
                this.plaintiffInstitutionCode = plaintiffInstitutionCode;
        }

        public String getDefendantInstitutionCode() { return defendantInstitutionCode; }
        public void setDefendantInstitutionCode(String defendantInstitutionCode) {
                this.defendantInstitutionCode = defendantInstitutionCode;
        }


        public boolean isOnUsTransaction() { return onUsTransaction; }
        public void setOnUsTransaction(boolean onUsTransaction) { this.onUsTransaction = onUsTransaction; }

        /* Utility to infer onUs flag automatically */
        public void evaluateOnUsFlag() {
                this.onUsTransaction = (plaintiffInstitutionCode != null
                        && plaintiffInstitutionCode.equalsIgnoreCase(defendantInstitutionCode));
        }

        public String getBillerCode() {
                return billerCode;
        }

        public void setBillerCode(String billerCode) {
                this.billerCode = billerCode;
        }

        public OtherTransactionInfo getOtherTransactionInfo() {
                return otherTransactionInfo;
        }

        public void setOtherTransactionInfo(OtherTransactionInfo otherTransactionInfo) {
                this.otherTransactionInfo = otherTransactionInfo;
        }

        public LocalDateTime getTransactionDate() {
                return transactionDate;
        }

        public void setTransactionDate(LocalDateTime transactionDate) {
                this.transactionDate = transactionDate;
        }

        public BigDecimal getTransactionAmount() {
                return transactionAmount;
        }

        public void setTransactionAmount(BigDecimal transactionAmount) {
                this.transactionAmount = transactionAmount;
        }

        public TransactionAction getTransactionAction() {
                return transactionAction;
        }

        public void setTransactionAction(TransactionAction transactionAction) {
                this.transactionAction = transactionAction;
        }

        public PaymentRail getTransactionPaymentRail() {
                return transactionPaymentRail;
        }

        public void setTransactionPaymentRail(PaymentRail transactionPaymentRail) {
                this.transactionPaymentRail = transactionPaymentRail;
        }

        public TransactionInstrument getTransactionInstrument() {
                return transactionInstrument;
        }

        public void setTransactionInstrument(TransactionInstrument transactionInstrument) {
                this.transactionInstrument = transactionInstrument;
        }
}
