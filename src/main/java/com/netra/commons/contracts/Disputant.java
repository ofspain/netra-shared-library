package com.netra.commons.contracts;

import com.netra.commons.contracts.enums.DisputantType;

public interface Disputant extends Nameable{

    DisputantType getDisputantType();
}
