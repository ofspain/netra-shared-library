package com.netra.commons.models.outlet;

import lombok.Data;

import java.util.List;

@Data
public class Merchant extends AccessPoint{

    private List<SettlementAccount> settlementAccounts;

    // Locations under this merchant
    private List<MerchantLocation> locations;

    public AccessPointType getAccessPointType(){
        return AccessPointType.MERCHANT;
    }
}
