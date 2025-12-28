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
public class TransactionType{

    // what the transaction is doing (intent)
    @NotNull
    private TransactionAction action;

    // HOW money moves (network)
    @NotNull
    private PaymentRail paymentRail;

    // what was used to transact the transaction
    @NotNull
    private TransactionInstrument instrument;
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
