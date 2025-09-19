package com.netra.commons.models.outlet;

import lombok.Data;


@Data
public class Agent extends AccessPoint {

    private String location;                  // Address / LGA / State

    public AccessPointType getAccessPointType(){
        return AccessPointType.AGENT;
    }
}
