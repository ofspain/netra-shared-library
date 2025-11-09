package com.netra.commons.models;

import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.DisputantType;
import com.netra.commons.enums.Facilitator;

public class FacilitatorDisputant implements Disputant {

    private Identity identity;
    private String name;

    private Facilitator facilitator;//can only be merchant or agent
    @Override
    public Identity getIdentity() {
        return identity;
    }

    @Override
    public DisputantType getDisputantType() {
        return DisputantType.DISPUTANTFACILITATOR;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }
}
