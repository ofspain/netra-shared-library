package com.netra.commons.models.outlet;

import lombok.Data;

import java.util.List;

@Data
public class Merchant extends RetailAccessPoint{

    private List<SettlementAccount> settlementAccounts;

    public AccessPointType getAccessPointType(){
        return AccessPointType.MERCHANT;
    }
}
