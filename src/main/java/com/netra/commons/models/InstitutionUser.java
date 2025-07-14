package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Disputant;
import com.netra.commons.contracts.enums.DisputantType;
import lombok.Data;

@Data
public class InstitutionUser extends BaseEntity implements Disputant, DisableAble {

    private String name;
    private Boolean disabled;
    private FinancialInstitution institution;
    private String email;

    @Override
    public DisputantType getDisputantType() {
        return DisputantType.INSTITUTIONUSER;
    }
}
