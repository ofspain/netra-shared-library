package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.enums.DisputantType;

public abstract class BaseUser extends BaseEntity implements DisableAble, Disputant {
        public abstract Identity getIdentity();

        @Override
        public DisputantType getDisputantType(){
                return null;
        }
}
