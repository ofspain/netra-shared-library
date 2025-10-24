package com.netra.commons.models.outlet;

public class SelfAccessPoint extends AccessPoint{
    @Override
    public AccessPointType getAccessPointType() {
        return AccessPointType.SELF;
    }
}
