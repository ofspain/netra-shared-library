package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import com.netra.commons.contracts.enums.TransactionChannel;
import lombok.Data;

import java.util.List;

@Data
public class TransactionType extends BaseEntity implements Nameable, DisableAble{
    //unique
    private String name;

    private Boolean disabled;
    private String description;
    private List<TransactionChannel> channels;
    //unique
    private String code;

    private List<TriageDecisionRule> triageDecisionRules;
    private List<TriageRequestRule> requestValidationRules;
}
