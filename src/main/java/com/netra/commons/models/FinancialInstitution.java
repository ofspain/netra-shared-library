package com.netra.commons.models;

import com.netra.commons.models.contracts.Domain;
import com.netra.commons.models.enums.DomainType;
import lombok.Data;


public class FinancialInstitution implements Domain {
    private String name;
    private String code;
    private String domainCode;

    private Boolean disabled;


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public DomainType getType() {
        return DomainType.FINANCIAL_INSTITUTION;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }




   public void disable(){
       this.disabled = true;
   }

    public void enable(){
       this.disabled = false;
    }
}
