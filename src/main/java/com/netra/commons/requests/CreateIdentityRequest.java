package com.netra.commons.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.netra.commons.enums.DomainType;
import com.netra.commons.models.Identity;
import com.netra.commons.models.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class CreateIdentityRequest {
    private String username;
    private String password;
    private Boolean disabled;
    private String domainCode;
    private DomainType domainType;
    private Boolean locked = Boolean.FALSE;

    private Set<Role> roles = new HashSet<>();

    // ✅ Convenience constructor
    public CreateIdentityRequest(Identity identity) {
        if (identity != null) {
            this.username = identity.getUsername();
            this.password = identity.getPassword(); // assuming you still have access here internally
            this.disabled = identity.getDisabled();
            this.domainCode = identity.getDomainCode();
            this.domainType = identity.getDomainType();
            this.locked = identity.getLocked();
            this.roles = identity.getRoles() != null
                    ? new HashSet<>(identity.getRoles())
                    : new HashSet<>();
        }
    }

    // Default constructor still available for deserialization
    public CreateIdentityRequest() {}

    public Identity toIdentity() {
        Identity identity = new Identity();

        identity.setUsername(this.username);
        identity.setPassword(this.password);
        identity.setDisabled(this.disabled != null ? this.disabled : Boolean.FALSE);
        identity.setDomainCode(this.domainCode);
        identity.setDomainType(this.domainType);
        identity.setLocked(this.locked != null ? this.locked : Boolean.FALSE);

        if (this.roles != null) {
            identity.setRoles(new HashSet<>(this.roles));
        } else {
            identity.setRoles(new HashSet<>());
        }

        return identity;
    }

}

