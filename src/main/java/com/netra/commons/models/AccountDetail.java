package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.AccountType;
import lombok.Data;

@Data
public class AccountDetail extends BaseEntity implements DisableAble {
    private String registeredPhone;
    private String registeredEmail;
    private String accountNumber;

    private Boolean disabled;

    private AccountType accountType;

    private FinancialInstitution issuingInstitution;

    private CardDTO card;


}
