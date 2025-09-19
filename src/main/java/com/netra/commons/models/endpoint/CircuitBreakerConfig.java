package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CircuitBreakerConfig {
    private int failureThreshold = 5;
    private long resetTimeoutMillis = 60000;
}
