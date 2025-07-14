package com.netra.commons.models.contracts;

import com.netra.commons.models.enums.DisputantType;

public interface Disputant extends Nameable{

    DisputantType getDisputantType();
}
