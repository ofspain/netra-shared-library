package com.netra.commons.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netra.commons.contracts.Nameable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class RoleTemplate extends BaseEntity implements Nameable{
    private String name;
    private String description;
    private List<Long> roleIds = new ArrayList<>();

    @JsonIgnore
    public List<Long> getRoleIds() {
        return roleIds;
    }

    @JsonProperty("roles")
    public void setRoles(List<Role> roles) {
        this.roleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }
}
