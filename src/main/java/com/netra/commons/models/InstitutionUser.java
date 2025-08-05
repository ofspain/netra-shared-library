package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.DisputantType;
import com.netra.commons.enums.DomainType;
import lombok.Data;

@Data
public class InstitutionUser extends BaseEntity implements Disputant, DisableAble {

    private String name;
    private Boolean disabled;
    private FinancialInstitution institution;
    private String email;

    private Identity identity;

    public DomainType domainType = DomainType.FINANCIAL_INSTITUTION;
}
