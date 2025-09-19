package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RetryConfig {
    private int maxAttempts = 3;
    private long initialDelayMillis = 200;
    private double multiplier = 2.0;
    private long maxDelayMillis = 2000;
}
