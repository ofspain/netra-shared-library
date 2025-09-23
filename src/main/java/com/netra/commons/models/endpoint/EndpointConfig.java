package com.netra.commons.models.endpoint;

import com.netra.commons.enums.DomainType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
public class EndpointConfig {
    private Long id; // uuid or natural key
    private NetworkConfig network = new NetworkConfig();
    private SecurityConfig security = new SecurityConfig();
    private Map<OperationType, EndpointDetail> endpoints = new HashMap<>(); // key = operation name (e.g. "uniqueTransaction")
    private ResilienceConfig resilience = new ResilienceConfig();
    private Map<String, Object> metadata = new HashMap<>(); // extensibility bag (tags, owner, version)

    //owner's identity
    private Long domainOwnerId;
    private String domainCode;    // e.g. MONNIFY
    private DomainType domainType;
    private String description;

    public enum OperationType{


        UNIQUE_TRANSACTION_SEARCH("uniqueTransaction"), BULK_TRANSACTION_SEARCH("bulkTransaction");
        private final String value;
        OperationType(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }
}

