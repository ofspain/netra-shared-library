package com.netra.commons.models.rules;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.enums.DisputeRuleType;
import com.netra.commons.models.BaseEntity;
import lombok.Data;

@Data
public class DisputeRule extends BaseEntity implements Nameable, DisableAble {

    private DisputeRuleType ruleType;
    private String name;

    private Boolean disabled;
}
