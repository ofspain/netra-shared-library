package com.netra.commons.models;

import com.netra.commons.enums.AccountType;
import lombok.Data;

@Data
public class AccountDetail extends BaseEntity{
    private String registeredPhone;
    private String registeredEmail;
    private String accountNumber;

    private AccountType accountType;

    private FinancialInstitution issuingInstitution;

    private CardDTO card;


}
