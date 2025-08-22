package com.netra.commons.contracts;

import com.netra.commons.enums.DomainType;

public interface Domain extends Nameable, DisableAble{

    String getCode(); //official code as used by regulator nation's eg cbn
    DomainType getDomainType();
    String getDomainCode(); //institution's customized code as usd in their official email first.lats@xyz.com!!!

    void setCode(String code);
    void setDomainCode(String domainCode);
}
