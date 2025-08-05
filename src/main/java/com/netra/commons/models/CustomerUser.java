package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.DisputantType;
import com.netra.commons.enums.DomainType;
import lombok.Data;

import java.util.List;

@Data
public class CustomerUser extends BaseEntity implements Disputant, DisableAble {

    private String name;
    private Boolean disabled;

    private List<AccountDetail> accounts;
    private String userPhone;//unique
    private String userEmail;
    private Identity identity;

    public DomainType domainType = DomainType.CUSTOMER;
    public String domainCode = Identity.CUSTOMERUSER_DOMAINCODE;
}
