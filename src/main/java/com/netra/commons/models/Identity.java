package com.netra.commons.models;

import com.netra.commons.contracts.DisableAble;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class Identity extends BaseEntity implements DisableAble {
    public static final String CUSTOMERUSER_DOMAINCODE = "THSCUDC";
    private String username;
    private String password;
    private Boolean disabled;
    private String domainCode;//todo: assign a constant domain code to all customeruser
    private LocalDateTime passwordLastChanged;
    private LocalDateTime lastLogin;
    private Boolean locked = Boolean.FALSE;

    private Set<Role> roles;

}
