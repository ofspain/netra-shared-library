package com.netra.commons.models.service.outlet;

import com.netra.commons.models.service.outlet.SettlementAccount;
import lombok.Data;

@Data
public class Terminal {
    private String terminalId;      // TID from acquirer
    private String model;           // Device model
    private String serialNumber;    // Device serial

    private SettlementAccount settlementAccountOverride; // Optional per-terminal override
}
