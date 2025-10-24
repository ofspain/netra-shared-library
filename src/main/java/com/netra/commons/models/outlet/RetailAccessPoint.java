package com.netra.commons.models.outlet;

import com.netra.commons.models.Identity;
import lombok.Data;

import java.util.List;

@Data
public abstract class RetailAccessPoint extends AccessPoint {
    private List<AccessPointLocation> locations;

    private Identity identity;
}

