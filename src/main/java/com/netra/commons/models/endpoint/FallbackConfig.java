package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FallbackConfig {
    private FallbackType type = FallbackType.EXCEPTION;
    private String value;

    public enum FallbackType { STATIC_RESPONSE, REDIRECT_ENDPOINT, EXCEPTION }
}



