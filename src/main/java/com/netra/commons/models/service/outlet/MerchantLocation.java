package com.netra.commons.models.service.outlet;

import com.netra.commons.enums.TransactionInstrument;
import lombok.Data;

import java.util.List;

@Data
public class MerchantLocation {
    private String locationId;      // Unique within merchant
    private String address;
    private String lga;
    private String state;

    private SettlementAccount defaultSettlementAccount; // Optional location-level default
    private List<Terminal> terminals;                    // Terminals at this location

    private List<TransactionInstrument> acceptedPaymentMethods;

}
