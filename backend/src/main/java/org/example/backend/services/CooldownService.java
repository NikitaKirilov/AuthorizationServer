package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.ActionCooldownException;
import org.example.backend.models.enums.CooldownAction;
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

    public void acquire(CooldownAction cooldownAction, String userId) {
        String key = buildKey(cooldownAction, userId);

        long cooldown = cooldownAction.getCooldown();
        long expiresAt = Instant.now().plusSeconds(cooldown).getEpochSecond();

        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, expiresAt, cooldown, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(success)) {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            throw new ActionCooldownException(cooldownAction.getName(), ttl);
        }
    }

    public boolean isBlocked(CooldownAction cooldownAction, String userId) {
        String key = buildKey(cooldownAction, userId);
        long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl > 0;
    }

    private String buildKey(CooldownAction cooldownAction, String userId) {
        return PREFIX + DELIMITER + cooldownAction.getName() + DELIMITER + userId;
    }
}
