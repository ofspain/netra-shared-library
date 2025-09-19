package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ResilienceConfig {
    private RetryConfig retry = new RetryConfig();
    private FallbackConfig fallback = new FallbackConfig();
    private CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();
}
