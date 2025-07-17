package com.netra.commons.contracts;

import com.netra.commons.enums.DomainType;

public interface Domain extends Nameable, DisableAble{

    String getCode();
    DomainType getDomainType();
    String getDomainCode();

    void setCode(String code);
    void setDomainCode(String domainCode);
}
