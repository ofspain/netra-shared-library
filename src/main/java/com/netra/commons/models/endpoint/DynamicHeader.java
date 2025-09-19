package com.netra.commons.models.endpoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DynamicHeader {
    private String name;
    private boolean required = false;
    private String description;
}
