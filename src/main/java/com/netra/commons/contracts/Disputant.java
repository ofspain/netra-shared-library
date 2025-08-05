package com.netra.commons.contracts;

import com.netra.commons.enums.DisputantType;
import com.netra.commons.models.Identity;

public interface Disputant extends Nameable{
    Identity getIdentity();

    DisputantType getDisputantType();
}
