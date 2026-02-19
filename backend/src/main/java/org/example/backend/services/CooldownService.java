package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.ActionCooldownException;
import org.example.backend.models.Action;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CooldownService {

    private static final String DELIMITER = ":";
    private static final String PREFIX = "sso:cooldowns";

    private final RedisOperations<Object, Object> redisTemplate;

    public void acquire(Action action, String userId) {
        String key = this.buildKey(action, userId);

        long cooldown = action.getCooldown();
        long expiresAt = Instant.now().plusSeconds(cooldown).getEpochSecond();

        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, expiresAt, cooldown, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(success)) {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            throw new ActionCooldownException(action.getName(), ttl);
        }
    }

    private String buildKey(Action action, String userId) {
        return PREFIX + DELIMITER + action.getName() + DELIMITER + userId;
    }
}
