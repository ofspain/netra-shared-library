package com.netra.commons.models;

import lombok.Data;


public class SystemUser extends BaseUser{

    private Boolean disabled;
    private String name;
    @Override
    public Boolean getDisabled() {
        return false;
    }

    @Override
    public void setDisabled(Boolean disable) {
        disabled = false;
    }

    @Override
    public String getName() {
        return Identity.systemIdentity().getUsername();
    }

    @Override
    public void setName(String name) {
        this.name = Identity.systemIdentity().getUsername();
    }

    @Override
    public Identity getIdentity() {
        return Identity.systemIdentity();
    }
}
