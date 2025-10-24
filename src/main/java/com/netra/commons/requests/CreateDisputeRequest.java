package com.netra.commons.requests;

import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.ApplicationChannel;
import com.netra.commons.enums.DisputeMode;
import com.netra.commons.enums.DisputeAmountType;
import com.netra.commons.models.AccountDetail;
import com.netra.commons.models.Evidence;
import com.netra.commons.models.Transaction;
import com.netra.commons.models.TransactionParticipant;
import com.netra.commons.models.outlet.AccessPoint;
import com.netra.commons.validators.annotations.ValidDisputeRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ValidDisputeRequest
public class CreateDisputeRequest {

    private Disputant initiator; // Who is initiating the dispute
    private Transaction transaction; // The disputed transaction
    private List<TransactionParticipant> participants; // All entities involved
    private List<Evidence> evidences; // Supporting documents
    private DisputeAmountType disputeAmountType; // FULL or PARTIAL
    private DisputeMode mode; // CHARGEBACK, REFUND, GOOD_FAITH, etc.
    private String note; // Free-text description of the issue
    private BigDecimal disputedAmount;
    private AccessPoint accessPoint;

    private AccountDetail accountDetail; //for customeruser

    private ApplicationChannel applicationChannel;

}
