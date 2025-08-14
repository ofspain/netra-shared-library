package com.netra.commons.models.service.outlet;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.TransactionInstrument;
import com.netra.commons.models.BaseEntity;
import com.netra.commons.models.FinancialInstitution;
import com.netra.commons.models.Identity;
import lombok.Data;

import java.util.List;

@Data
public abstract class AccessPoint extends BaseEntity implements Nameable, DisableAble {

    private FinancialInstitution institution; // Bank or Acquirer
    private String accessPointCode;           // Unique code in Themista
    private String institutionCode;           // Unique code in bank/acquirer system
    private String name;                      // Trading/operating name
    private Boolean disabled;                 // Active/inactive status
    private Identity identity;                 // NIN, BVN, CAC, etc.
    private ContactDetails contactDetails;     // Phone, email, etc.
    private List<TransactionInstrument> defaultPaymentMethods; // Defaults at this level if applicable

    public abstract AccessPointType getAccessPointType();

    public enum AccessPointType{
        AGENT, MERCHANT
    }
}

