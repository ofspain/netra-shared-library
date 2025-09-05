package com.netra.commons.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data

public class RefreshToken extends BaseEntity{


    private String token;

    private Identity identity;

    private LocalDateTime expiresAt;

    private boolean revoked = false;

    private LocalDateTime updatedAt;


    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }
}