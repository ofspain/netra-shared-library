package com.netra.commons.models;

import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.ApplicationChannel;
import com.netra.commons.enums.DisputeState;

import java.time.LocalDateTime;

public class Dispute extends BaseEntity{

        private Transaction transaction;
        private LocalDateTime disputeMarkedLegitTime;
        private DisputeState currentState;

        private DisputeState previousState;

        private ApplicationChannel createdVia;
        private Disputant createdBy;

        private String note;

        private String issuerCode;
        private String acquirerCode;

        private String merchantCode;

        private String beneficiaryCode;

        private String switcherCode;

        private boolean locked;


        private boolean isFinalized; //reaches any end state
        private boolean isResolved; //reaches a definite resolution

        private boolean resolvedInCustomerFavor;

        public Transaction getTransaction() {
                return transaction;
        }

        public void setTransaction(Transaction transaction) {
                this.transaction = transaction;
        }

        public LocalDateTime getDisputeMarkedLegitTime() {
                return disputeMarkedLegitTime;
        }

        public void setDisputeMarkedLegitTime(LocalDateTime disputeMarkedLegitTime) {
                this.disputeMarkedLegitTime = disputeMarkedLegitTime;
        }

        public DisputeState getCurrentState() {
                return currentState;
        }

        public void setCurrentState(DisputeState currentState) {
                this.currentState = currentState;
        }

        public DisputeState getPreviousState() {
                return previousState;
        }

        public void setPreviousState(DisputeState previousState) {
                this.previousState = previousState;
        }

        public ApplicationChannel getCreatedVia() {
                return createdVia;
        }

        public void setCreatedVia(ApplicationChannel createdVia) {
                this.createdVia = createdVia;
        }

        public Disputant getCreatedBy() {
                return createdBy;
        }

        public void setCreatedBy(Disputant createdBy) {
                this.createdBy = createdBy;
        }

        public String getNote() {
                return note;
        }

        public void setNote(String note) {
                this.note = note;
        }

        public String getIssuerCode() {
                return issuerCode;
        }

        public void setIssuerCode(String issuerCode) {
                this.issuerCode = issuerCode;
        }

        public String getAcquirerCode() {
                return acquirerCode;
        }

        public void setAcquirerCode(String acquirerCode) {
                this.acquirerCode = acquirerCode;
        }

        public String getMerchantCode() {
                return merchantCode;
        }

        public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
        }


        public String getSwitcherCode() {
                return switcherCode;
        }

        public void setSwitcherCode(String switcherCode) {
                this.switcherCode = switcherCode;
        }

        public String getBeneficiaryCode() {
                return beneficiaryCode;
        }

        public void setBeneficiaryCode(String beneficiaryCode) {
                this.beneficiaryCode = beneficiaryCode;
        }

        public boolean isFinalized() {
                return isFinalized;
        }

        public void setFinalized(boolean finalized) {
                isFinalized = finalized;
        }

        public boolean isResolved() {
                return isResolved;
        }

        public void setResolved(boolean resolved) {
                isResolved = resolved;
        }

        public boolean isResolvedInCustomerFavor() {
                return resolvedInCustomerFavor;
        }

        public void setResolvedInCustomerFavor(boolean resolvedInCustomerFavor) {
                this.resolvedInCustomerFavor = resolvedInCustomerFavor;
        }

        public boolean isLocked() {
                return locked;
        }

        public void setLocked(boolean locked) {
                this.locked = locked;
        }


}
