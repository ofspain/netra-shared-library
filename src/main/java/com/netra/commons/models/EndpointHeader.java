package com.netra.commons.models;

import java.time.LocalDateTime;

public class EndpointHeader extends BaseEntity {

    private String headerName;
    private String headerValue;

    private EndpointType endpointType; // Determines if it's for UNIQUE or MULTIPLE

    private Long endpointConfigId; // Foreign key reference

    public enum EndpointType {
        UNIQUE,
        MULTIPLE
    }

    // ========= Getters and Setters ========= //

    // (Omitted here for brevity — generate with your IDE or Lombok if allowed)
}

