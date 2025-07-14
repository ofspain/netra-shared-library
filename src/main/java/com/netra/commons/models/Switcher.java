package com.netra.commons.models;

import com.netra.commons.models.contracts.Domain;
import com.netra.commons.models.enums.DomainType;

public class Switcher   extends BaseEntity implements Domain {
    private String name;
    private String code;
    private String domainCode;

    private Boolean disabled;

    @Override
    public Boolean getDisabled() {
        return this.disabled;
    }

    @Override
    public void setDisabled(Boolean disable) {
        this.disabled = disable;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public DomainType getDomainType() {
        return DomainType.FINANCIAL_INSTITUTION;
    }

    @Override
    public String getDomainCode() {
        return this.domainCode;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
