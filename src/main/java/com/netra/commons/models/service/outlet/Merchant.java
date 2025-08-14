package com.netra.commons.models.service.outlet;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.TransactionInstrument;
import com.netra.commons.models.*;
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
