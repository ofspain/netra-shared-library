package com.netra.commons.models;

import lombok.Data;

@Data
public class DisputeJournal extends BaseEntity {

    private String disputeLogCode;
    private String comment;
    private String addedBy;
}
