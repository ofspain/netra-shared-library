package com.netra.commons.models.service.outlet;

import com.netra.commons.models.FinancialInstitution;
import lombok.Data;

@Data
public class SettlementAccount {
    private String accountNumber;
    private FinancialInstitution financialInstitution;
    private String currency;    // e.g., NGN, USD
    private Boolean isDefault;  // Flag for default account
}