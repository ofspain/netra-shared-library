package com.netra.commons.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RedisTemplate<String, String> redis;

    public String getBearerToken(String domain) {
        String key = "token:" + domain;
        String token = redis.opsForValue().get(key);
        if (token != null) return token;

        // simulate token retrieval
        token = "mocked_token_for_" + domain;
        redis.opsForValue().set(key, token, Duration.ofMinutes(30));
        return token;
    }

    public String getBasicCredentials(String domain) {
        return Base64.getEncoder().encodeToString(("user_" + domain + ":pass").getBytes());
    }

    public String getApiKey(String domain) {
        return "api-key-for-" + domain;
    }
}

