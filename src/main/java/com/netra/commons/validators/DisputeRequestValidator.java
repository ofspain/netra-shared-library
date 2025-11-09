package com.netra.commons.validators;

import com.netra.commons.enums.*;
import com.netra.commons.util.BasicUtil;
import com.netra.commons.validators.annotations.ValidDisputeRequest;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.models.*;
import com.netra.commons.requests.CreateDisputeRequest;
import com.netra.commons.requests.util.TransactionRailDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DisputeRequestValidator implements ConstraintValidator<ValidDisputeRequest, CreateDisputeRequest> {

    @Override
    public boolean isValid(CreateDisputeRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            throw new IllegalArgumentException("CreateDisputeRequest cannot be null");
        }

        boolean valid = true;

        // Utility: disable default messages
        context.disableDefaultConstraintViolation();

        if(null == request.getApplicationChannel()){
            context.buildConstraintViolationWithTemplate("Channel of submit is required.")
                    .addPropertyNode("applicationChannel")
                    .addConstraintViolation();
            valid = false;
        }

        if(null == request.getTransactionAmount() || request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0 ){
            context.buildConstraintViolationWithTemplate("Valid transaction amount is required.")
                    .addPropertyNode("transactionAmount")
                    .addConstraintViolation();
            valid = false;
        }
        LocalDateTime transactionDate = request.getTransactionDate();
        if(null == transactionDate){
            context.buildConstraintViolationWithTemplate("Valid transaction date is required")
                    .addPropertyNode("transactionDate")
                    .addConstraintViolation();
            valid = false;
        }else if (transactionDate.toLocalDate().isAfter(LocalDate.now())) {
            context.buildConstraintViolationWithTemplate("Transaction date cannot be in the future.")
                    .addPropertyNode("transactionDate")
                    .addConstraintViolation();
            valid = false;
        }



        if(null == request.getTransactionAction()){
            context.buildConstraintViolationWithTemplate("Valid transaction action(mode) is required")
                    .addPropertyNode("transactionAction")
                    .addConstraintViolation();
            valid = false;
        }

        TransactionRailDTO transactionRail = request.getTransactionRail();

        if(null == transactionRail){
            context.buildConstraintViolationWithTemplate("Valid transaction rail is required")
                    .addPropertyNode("transactionRail")
                    .addConstraintViolation();
            valid = false;
        }else{
            TransactionInstrument instrument = transactionRail.getInstrument();
            PaymentRail paymentRail = transactionRail.getPaymentRail();

            if(null == instrument){
                context.buildConstraintViolationWithTemplate("Valid transaction instrument is required")
                        .addPropertyNode("transactionRail.instrument")
                        .addConstraintViolation();
                valid = false;
            }else if(instrument.equals(TransactionInstrument.WEB_PORTAL) || instrument.equals(TransactionInstrument.MOBILE_APP)){
                AccountDetail beneficiaryAccount = request.getBeneficiaryAccount();
                if(null == beneficiaryAccount){
                    context.buildConstraintViolationWithTemplate("Valid Beneficiary Account is required for internet based transaction")
                            .addPropertyNode("beneficiaryAccount")
                            .addConstraintViolation();
                    valid = false;
                }else{
                    if(!BasicUtil.validString(beneficiaryAccount.getAccountNumber())){//may add validation of account number....lenght and all numeric
                        context.buildConstraintViolationWithTemplate("Valid Account Number is required for internet based transaction")
                                .addPropertyNode("beneficiaryAccount.accountNumber")
                                .addConstraintViolation();
                        valid = false;
                    }
                    if(null == beneficiaryAccount.getIssuingInstitution()){
                        context.buildConstraintViolationWithTemplate("Valid Financial Institution is required for internet based transaction")
                                .addPropertyNode("beneficiaryAccount.issuingInstitution")
                                .addConstraintViolation();
                        valid = false;
                    }
                }
            }

            if(null == paymentRail ){
                context.buildConstraintViolationWithTemplate("Valid payment rail is required")
                        .addPropertyNode("transactionRail.paymentRail")
                        .addConstraintViolation();
                valid = false;
            }

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
            valid = false;
        }

        DisputantType type = initiator.getDisputantType();

        switch (type) {
            case CUSTOMERUSER:

                if (request.getAffectedAccount() == null) {
                    context.buildConstraintViolationWithTemplate("Account details are required for CUSTOMERUSER.")
                            .addPropertyNode("affectedAccount")
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
                CreateDisputeRequest.DisputingAs disputingAs = request.getDisputingAs();
                if(null == disputingAs){
                    context.buildConstraintViolationWithTemplate("Disputing AS is required for INSTITUTIONUSER.")
                            .addPropertyNode("disputingAs")
                            .addConstraintViolation();
                    valid = false;
                }else{
                   if(disputingAs.equals(CreateDisputeRequest.DisputingAs.ISSUER)){
                     //must issuer always know the acquirer?
                   }

                    if(disputingAs.equals(CreateDisputeRequest.DisputingAs.ISSUER)){

                    }
                }

                break;
            case DISPUTANTFACILITATOR:
                FinancialInstitution acquirer = request.getAcquirer();
                if(null == acquirer){
                    context.buildConstraintViolationWithTemplate("Acquiring Institution is required for a disputant facilitator")
                            .addPropertyNode("acquirer")
                            .addConstraintViolation();
                    valid = false;
                }

            default:
                // Unknown or unsupported disputant type can be handled here if necessary
                break;
        }



        return valid;
    }

}

