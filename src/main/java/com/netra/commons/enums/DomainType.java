package com.netra.commons.enums;

public enum DomainType {
    FINANCIAL_INSTITUTION("FINANCIAL INSTITUTION"),
    SWITCH("SWITCH"),
    REGULATOR("REGULATOR"),

    INTERNAL("INTERNAL"),

    SYSTEM("SYSTEM"),


    CUSTOMER("CUSTOMER");


    private final String uiName;

    public String getUiName() {
        return uiName;
    }

    DomainType(String uiName){
        this.uiName =uiName;
    }


}
