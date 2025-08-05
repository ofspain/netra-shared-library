package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import com.netra.commons.contracts.Nameable;
import lombok.Data;

@Data
public class TransactionSource extends BaseEntity implements Nameable, DisableAble {

    private String name;                     // e.g., "Interswitch Transaction Sto
    private String baseUrl;                  // Base URL for all endpoints
    private String businessCode;             // Optional: useful for lookup/authorization
    private boolean requiresDomainMapping;   // Indicates if institution/domain mapping is needed
    private boolean requiresDuplicateCheck;  // Indicates if deduplication logic applies\
    private Boolean disabled;

    private Switcher switcher;

    // Endpoints (read-only for now)
    private EndpointConfig searchTransaction; // For searching by multiple fields
    private EndpointConfig getTransaction;    // For fetching by txn ID or reference
}

