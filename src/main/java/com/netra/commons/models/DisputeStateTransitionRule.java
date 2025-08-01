package com.netra.commons.models;

import com.netra.commons.enums.DisputeState;
import lombok.Data;

import java.util.Map;

@Data
public class DisputeStateTransitionRule {

    private DisputeState currentState;

    Map<DisputeState, Integer> allowedTransitionSateWithDuration;
}
