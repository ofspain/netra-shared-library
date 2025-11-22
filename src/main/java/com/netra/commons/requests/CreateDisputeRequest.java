package com.netra.commons.requests;

import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.ApplicationChannel;
import com.netra.commons.enums.DisputeMode;
import com.netra.commons.enums.DisputeAmountType;
import com.netra.commons.enums.TransactionAction;
import com.netra.commons.models.*;
import com.netra.commons.requests.util.TransactionRailDTO;
import com.netra.commons.validators.annotations.ValidDisputeRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ValidDisputeRequest
public class CreateDisputeRequest {

    private BaseUser initiator;//host id, disputant_type

    private DisputingAs disputingAs;

    private List<String> evidences;//base 64 encoded string with all metadata...to get mime
    private DisputeAmountType disputeAmountType;
    private DisputeMode mode;
    private String note;
    private BigDecimal disputedAmount;

    private AccountDetail affectedAccount;
    private AccountDetail beneficiaryAccount;//only for web/mobile based

    private ApplicationChannel applicationChannel;

    private TransactionRailDTO transactionRail;

    private TransactionAction transactionAction;
    private FacilitatorDisputant facilitatorDisputant;
    private FinancialInstitution acquirer;//for customer user, evidence should be used, for issuer, then it must be supplied
    private FinancialInstitution issuer;//for customer user, this is derived from affected account, for acquirer, then it must be supplied(may be for goodfaith)
    private FinancialInstitution aggregator;//for customer user, evidence should be used, for issuer, then it must be supplied
    private TransactionErrorDTO error;

    private String issuerTransactionRef;
    private String stan;
    private String rrn;
    private String authCode;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private DisputeMode disputeMode;


    public enum DisputingAs{
        ISSUER, ACQUIRER
    }

}
