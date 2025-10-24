package com.netra.commons.models.outlet;

import com.netra.commons.enums.TransactionInstrument;
import com.netra.commons.models.FinancialInstitution;

public class AccessPointInstrument {
    private TransactionInstrument transactionInstrument;
    private FinancialInstitution financialInstitution;


    private String instrumentId;      // TID from acquirer
    private String model;           // Device model
    private String serialNumber;    // Device serial

    private SettlementAccount settlementAccountOverride; // Optional per-terminal override
}
