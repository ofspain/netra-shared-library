package com.netra.commons.models.endpoint;

import com.netra.commons.enums.DomainType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
public class EndpointConfig {
    private Long id; // uuid or natural key
    private NetworkConfig network;
    private SecurityConfig security;
    private Map<OperationType, EndpointDetail> endpoints; // key = operation name (e.g. "uniqueTransaction")
    private ResilienceConfig resilience;
    private Map<String, Object> metadata; // extensibility bag (tags, owner, version)

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

