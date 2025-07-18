package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.CurrencyType;

public class Currency extends BaseEntity implements Nameable, DisableAble{

    private String code;                // e.g.,ISO CODE "NGN", "USD", "BTC"
    private String name;                // e.g., "Naira", "US Dollar", "Bitcoin"
    private String symbol;              // e.g., "₦", "$", "₿"
    private CurrencyType type;          // FIAT, CRYPTO, CBDC, TOKEN
    private int decimalPrecision;       // e.g., 2 for NGN, 8 for BTC
    private boolean disabled;           // Used for enabling/disabling currencies
    // --- Constructors ---
    public Currency() {}

    public Currency(String code, String name, String symbol, CurrencyType type,
                    int decimalPrecision, boolean disabled) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.type = type;
        this.decimalPrecision = decimalPrecision;
        this.disabled = disabled;
    }

    // --- Getters and Setters ---
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public CurrencyType getType() {
        return type;
    }

    public void setType(CurrencyType type) {
        this.type = type;
    }

    public int getDecimalPrecision() {
        return decimalPrecision;
    }

    public void setDecimalPrecision(int decimalPrecision) {
        this.decimalPrecision = decimalPrecision;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}

