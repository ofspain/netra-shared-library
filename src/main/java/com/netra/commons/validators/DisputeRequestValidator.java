package com.netra.commons.validators;

import com.netra.commons.annotations.ValidDisputeRequest;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.DisputantType;
import com.netra.commons.enums.DisputeAmountType;
import com.netra.commons.enums.TransactionParticipationRole;
import com.netra.commons.models.*;
import com.netra.commons.requests.CreateDisputeRequest;
import com.netra.commons.requests.util.TransactionRailDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.List;

public class DisputeRequestValidator implements ConstraintValidator<ValidDisputeRequest, CreateDisputeRequest> {

    @Override
    public boolean isValid(CreateDisputeRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return false;
        }

        boolean valid = true;

        // Utility: disable default messages
        context.disableDefaultConstraintViolation();

        // Rule 1: Must have ISSUER in participants
        if (!hasParticipantWithRole(request.getParticipants(), TransactionParticipationRole.ISSUER)) {
            context.buildConstraintViolationWithTemplate("At least one ISSUER participant is required.")
                    .addPropertyNode("participants")
                    .addConstraintViolation();
            valid = false;
        }

        // Rule 2: If PARTIAL, then disputedAmount must be valid
        DisputeAmountType disputeAmountType = request.getDisputeAmountType();
        if (disputeAmountType == DisputeAmountType.PARTIAL) {
            if (request.getDisputedAmount() == null || request.getDisputedAmount().compareTo(BigDecimal.ZERO) <= 0) {
                context.buildConstraintViolationWithTemplate("Disputed Amount must be provided for PARTIAL disputes.")
                        .addPropertyNode("disputedAmount")
                        .addConstraintViolation();
                valid = false;
            }
        }

        // Rule 3 & 4: Based on Disputant Type
        Disputant initiator = request.getInitiator();
        if (initiator == null) {
            context.buildConstraintViolationWithTemplate("Initiator is required.")
                    .addPropertyNode("initiator")
                    .addConstraintViolation();
            return false;
        }

        DisputantType type = initiator.getDisputantType();

        switch (type) {
            case CUSTOMERUSER:
                if (request.getAccountDetail() == null) {
                    context.buildConstraintViolationWithTemplate("Account details are required for CUSTOMERUSER.")
                            .addPropertyNode("accountDetail")
                            .addConstraintViolation();
                    valid = false;
                }

                if (request.getEvidences() == null || request.getEvidences().isEmpty()) {
                    context.buildConstraintViolationWithTemplate("At least one evidence is required for CUSTOMERUSER.")
                            .addPropertyNode("evidences")
                            .addConstraintViolation();
                    valid = false;
                }
                break;

            case INSTITUTIONUSER:
                if (!hasParticipantWithRole(request.getParticipants(), TransactionParticipationRole.ACQUIRER)) {
                    context.buildConstraintViolationWithTemplate("ACQUIRER participant is required for INSTITUTIONUSER.")
                            .addPropertyNode("participants")
                            .addConstraintViolation();
                    valid = false;
                }
                break;

            default:
                // Unknown or unsupported disputant type can be handled here if necessary
                break;
        }

        // Rule 5: Basic transaction validation
        Transaction txn = request.getTransaction();
        if (txn == null) {
            context.buildConstraintViolationWithTemplate("Transaction is required.")
                    .addPropertyNode("transaction")
                    .addConstraintViolation();
            valid = false;
        } else {
            if (txn.getAmount() == null || txn.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                context.buildConstraintViolationWithTemplate("Transaction amount must be positive.")
                        .addPropertyNode("transaction.amount")
                        .addConstraintViolation();
                valid = false;
            }
            if (txn.getTransactionRef() == null || txn.getTransactionRef().trim().isEmpty()) {
                context.buildConstraintViolationWithTemplate("Transaction reference is required.")
                        .addPropertyNode("transaction.transactionRef")
                        .addConstraintViolation();
                valid = false;
            }
            if (txn.getTransactionDate() == null) {
                context.buildConstraintViolationWithTemplate("Transaction date is required.")
                        .addPropertyNode("transaction.transactionDate")
                        .addConstraintViolation();
                valid = false;
            }

            TransactionRailDTO rail = txn.getTransactionRailDTO();
            if (rail == null || rail.getInstrumentId() == null || rail.getInstrumentId().isEmpty()) {
                context.buildConstraintViolationWithTemplate("Transaction instrument ID is required.")
                        .addPropertyNode("transaction.transactionRailDTO.instrumentId")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    private boolean hasParticipantWithRole(List<TransactionParticipant> participants, TransactionParticipationRole role) {
        if (participants == null) return false;
        return participants.stream().anyMatch(p -> p.getTransactionParticipationRole() == role);
    }
}

