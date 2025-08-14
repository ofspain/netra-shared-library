package com.netra.commons.contracts;

import com.netra.commons.enums.DomainType;

public interface Domain extends Nameable, DisableAble{

    String getCode(); //official code
    DomainType getDomainType();
    String getDomainCode(); //platform code

    void setCode(String code);
    void setDomainCode(String domainCode);
}
