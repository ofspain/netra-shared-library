package com.netra.commons.contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface DisableAble {

    Boolean getDisabled();
    void setDisabled(Boolean disable);

    default void disable(){
        setDisabled(true);
    }

    default void enable(){
        setDisabled(false);
    }

    @JsonIgnore
    default Boolean isEnabled(){

        return !getDisabled();
    }
}
