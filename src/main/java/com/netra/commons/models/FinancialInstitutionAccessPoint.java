package com.netra.commons.models;

import com.netra.commons.models.outlet.AccessPoint;
import com.netra.commons.models.outlet.AccessPointLocation;

import java.util.List;

import static com.netra.commons.models.outlet.AccessPoint.AccessPointType.INSTITUTION;

public class FinancialInstitutionAccessPoint extends AccessPoint {

    private FinancialInstitution institution;
    private List<AccessPointLocation> locations;


    @Override
    public AccessPointType getAccessPointType() {
        return INSTITUTION;
    }
}
