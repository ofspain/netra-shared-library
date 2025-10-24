package com.netra.commons.models.outlet;

import lombok.Data;

import java.util.List;

@Data
public class AccessPointLocation {
    private String locationId;      // Unique within merchant
    private String address;
    private String lga;
    private String state;

    private SettlementAccount defaultSettlementAccount; // Optional location-level default
    private List<AccessPointInstrument> terminals;                    // Terminal(s) at this location

}
