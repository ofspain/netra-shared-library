package com.netra.commons.models.contracts;

import com.netra.commons.models.enums.DomainType;

public interface Domain extends Nameable, DisableAble{

    String getCode();
    DomainType getDomainType();
    String getDomainCode();

    void setCode(String code);
    void setDomainCode(String domainCode);
}
