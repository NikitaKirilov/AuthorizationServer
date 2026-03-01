package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.AttemptAction;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AttemptsService {

    private static final String DELIMITER = ":";
    private static final String PREFIX = "sso:attempts";

    private final StringRedisTemplate redisTemplate;

    public void track(AttemptAction attemptAction, String id, String ip) {
        String key = this.buildKey(attemptAction, id, ip);
        redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, attemptAction.getCooldown(), TimeUnit.SECONDS);
    }

    public boolean isBlocked(AttemptAction attemptAction, String id, String ip) {
        String key = this.buildKey(attemptAction, id, ip);
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null && Long.parseLong(attempts) >= attemptAction.getAttempts();
    }

    public void reset(AttemptAction attemptAction, String id, String ip) {
        String key = this.buildKey(attemptAction, id, ip);
        redisTemplate.delete(key);
    }

    private String buildKey(AttemptAction attemptAction, String id, String ip) {
        return String.join(DELIMITER, PREFIX, attemptAction.getName(), id, ip);
    }
}
