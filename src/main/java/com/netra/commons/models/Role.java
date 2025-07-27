package com.netra.commons.models;

import com.netra.commons.contracts.Nameable;
import lombok.Data;

@Data
public class Role extends BaseEntity implements Nameable {
    private String name;

    private String description;
}
