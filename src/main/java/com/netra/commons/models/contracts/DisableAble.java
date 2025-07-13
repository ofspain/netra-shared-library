package com.netra.commons.models.contracts;

public interface DisableAble {

    Boolean getDisabled();

    void disable();

    void enable();

    default Boolean isEnabled(){
        return !getDisabled();
    }
}
