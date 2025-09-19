package com.netra.commons.models.endpoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaticHeader {
    private String name;
    private String value; // e.g. "Bearer ${vault:vk-123}" or "abc123"
    private boolean secret;       // true => templateValue must contain vault placeholder(s)
}
//for secret, we store template as value <*>${vault:location}<*> with ${*} replaced at runtime

