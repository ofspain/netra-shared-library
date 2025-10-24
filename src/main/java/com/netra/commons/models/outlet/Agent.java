package com.netra.commons.models.outlet;

import lombok.Data;

import java.util.List;


@Data
public class Agent  extends RetailAccessPoint{

    public AccessPointType getAccessPointType(){
        return AccessPointType.AGENT;
    }
}
