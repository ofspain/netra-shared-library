package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.DisputeMode;
import lombok.Data;

/**
 * the preliminary assessment of patients or casualties in order to
 * determine the urgency of their need for treatment and the nature of treatment required.
 * 1) DEFINE RULE TO VALIDATE REQUEST(used model below, valid pic, etc)
 * 2) DEFINE RULE TO MAKE DECISION(used concrete input like ref,date,etc
 */

@Data
public class TriageDecisionRule extends BaseEntity implements Nameable, DisableAble {
    private String name;
    private String description;
    private Boolean disabled;

    private DisputeMode suggestedMode;

    private String nextQueue;

    private String classification;

    private String jsonLogic;
    // The rule condition in JSONLogic format, store as text/blob in db

    /**
     * {
     *   "and": [
     *     { "==": [ { "var": "transaction.errorType" }, "FAILED_DEBIT" ] },
     *     { "==": [ { "var": "transaction.transactionType.code" }, "POS_PURCHASE" ] },
     *     { "in": [ "DEBIT_ALERT", { "var": "evidenceTypes" } ] },
     *     { "in": [ "POS_RECEIPT", { "var": "evidenceTypes" } ] }
     *   ]
     * }
     */

}
