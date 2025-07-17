package com.netra.commons.contracts;

import com.netra.commons.enums.DisputantType;

public interface Disputant extends Nameable{

    DisputantType getDisputantType();
}
