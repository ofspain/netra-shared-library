package com.netra.commons.models;

import com.netra.commons.enums.DisputeTransitionEvent;
import lombok.Data;

@Data
public class DisputeJournal extends BaseEntity {

    private Long disputeId;
    private String comment;
    private String addedBy;
    private DisputeTransitionEvent event;
}
