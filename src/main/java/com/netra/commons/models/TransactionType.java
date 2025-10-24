package com.netra.commons.models;


import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TransactionType extends BaseEntity implements Nameable, DisableAble {

    @NotBlank
    private String name;     // e.g., "POS Purchase", "Agency Banking Withdrawal"

    private Boolean disabled = false;

    private String description;

    // what the transaction is doing (intent)
    @NotNull
    private TransactionAction action;

    // allowable transaction instrument for this transaction type
    @NotNull
    private List<TransactionInstrument> instruments;

    // which network/rail processes it: may not be applicable for our use case
    @NotNull
    private List<TransactionProcessor> processors;

    // unique internal usage
    @NotBlank
    private String code;

    // unique external code (CBN/NIBSS) if applicable, can be null

    private String industryCode;
}

//{
//  "name": "Agency Banking Withdrawal",
//  "disabled": false,
//  "description": "Customer cashes out via agent POS terminal",
//  "action": "WITHDRAWAL",
//  "instruments": ["AGENCY_BANKING", "POS_DEVICE"],
//  "processors": ["POS_SWITCH", "WALLET_PROCESSOR"],
//  "code": "ABWDR"
//}
