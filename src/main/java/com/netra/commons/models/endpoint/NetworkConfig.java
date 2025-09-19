package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor

public class NetworkConfig {
    private String baseUrl;
    private int timeoutMillis = 5000;
    private boolean useProxy = false;
    private ProxyConfig proxy;
}
