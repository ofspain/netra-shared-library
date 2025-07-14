package com.netra.commons.contracts;

public interface DisableAble {

    Boolean getDisabled();
    void setDisabled(Boolean disable);

    default void disable(){
        setDisabled(true);
    }

    default void enable(){
        setDisabled(false);
    }

    default Boolean isEnabled(){

        return !getDisabled();
    }
}
