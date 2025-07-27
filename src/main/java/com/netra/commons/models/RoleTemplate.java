package com.netra.commons.models;

import com.netra.commons.contracts.Nameable;
import lombok.Data;

import java.util.Set;

@Data
public class RoleTemplate extends BaseEntity implements Nameable {

    private String name;//DOMAIN_ADMIN

    private Set<Role> roles;

    private String description;

    //this role automatically has its set<role> built into it
}
