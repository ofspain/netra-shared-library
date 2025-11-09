package com.netra.commons.models;


import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    // HOW money moves (network)
    @NotNull
    private PaymentRail paymentRail;

    // what was used to transact the transaction
    @NotNull
    private TransactionInstrument instrument;

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
