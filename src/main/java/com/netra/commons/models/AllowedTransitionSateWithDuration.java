package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.DisputeState;
import lombok.Data;

@Data
public class AllowedTransitionSateWithDuration extends BaseEntity implements DisableAble {

    private Boolean disabled;

    public enum StartingPoint {
        TRANSACTION_CREATION_DATE,
        TRANSACTION_SETTLED_DATE,
        DISPUTE_CREATION_DATE,
        DISPUTE_MARKED_LEGIT_DAY,


    }

    public enum DurationUnit {
        SECONDS,
        MINUTES,
        HOURS,
        DAYS,
        MONTHS,
        YEARS,
    }

    private Integer duration;
    private DisputeState toDisputeState;

    private DurationUnit durationUnit = DurationUnit.MINUTES;

    private StartingPoint startingPoint = StartingPoint.DISPUTE_MARKED_LEGIT_DAY;


}
