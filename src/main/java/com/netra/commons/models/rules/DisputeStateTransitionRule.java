package com.netra.commons.models.rules;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.DisputeState;
import com.netra.commons.models.AllowedTransitionSateWithDuration;
import com.netra.commons.models.BaseEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DisputeStateTransitionRule extends DisputeRule{


    private DisputeState currentState;



    List<AllowedTransitionSateWithDuration> allowedTransitionSateWithDuration;
}

/*
CREATE TABLE dispute_state_transition_rule (
    id BIGINT PRIMARY KEY REFERENCES base_entity(id),
    current_state dispute_state NOT NULL
);

CREATE TABLE dispute_state_transition_allowed (
    rule_id BIGINT REFERENCES dispute_state_transition_rule(id),
    next_state dispute_state NOT NULL,
    max_duration_minutes INTEGER NOT NULL,
    id bigreal(primary)
);
 */