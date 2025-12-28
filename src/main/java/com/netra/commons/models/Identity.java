package com.netra.commons.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netra.commons.contracts.DisableAble;
import com.netra.commons.enums.DomainType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class Identity extends BaseEntity implements DisableAble {
    public static final String CUSTOMERUSER_DOMAINCODE = "THSCUDC";
    public static final String SYSTEM_DOMAINCODE = "SYS";
    private String username;
    @JsonIgnore
    private String password;
    private Boolean disabled = Boolean.FALSE;

    //both these two will be used to identify the user on the other side of the console
    private String identityUuid;
    private String domainCode;
    private DomainType domainType;


    private LocalDateTime passwordLastChanged;
    private LocalDateTime lastLogin;
    private Boolean locked = Boolean.FALSE;

    private Set<Role> roles = new HashSet<>();


    //todo: assign a constant domain code to all customeruser
    //consider also defining an identity for system here making it publicly accessible

    public static Identity systemIdentity(){
        Identity system = new Identity();
        system.setUsername("system");
        system.setDisabled(false);
        system.setDomainCode(SYSTEM_DOMAINCODE);
        system.setDomainType(DomainType.SYSTEM);
        system.setIdentityUuid("<UUID>");

        return system;
    }

}
