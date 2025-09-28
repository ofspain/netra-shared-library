package com.netra.commons.models.endpoint;

import com.netra.commons.enums.DomainType;
import com.netra.commons.models.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
public class EndpointConfig extends BaseEntity {
    private NetworkConfig network = new NetworkConfig();
    private SecurityConfig security = new SecurityConfig();
    private List<EndpointDetail> endpoints = new ArrayList<>(); // key = operation name (e.g. "uniqueTransaction")
    private ResilienceConfig resilience = new ResilienceConfig();
    private Map<String, Object> metadata = new HashMap<>(); // extensibility bag (tags, owner, version)

    //owner's identity
    private Long domainOwnerId;
    private String domainOwnerCode;    // e.g. MONNIFY
    private DomainType domainOwnerType;
    private String description;
}

