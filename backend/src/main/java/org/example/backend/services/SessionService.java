package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    public List<Session> getUserSessions() {
        String currentId = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, ? extends Session> sessionMap = redisIndexedSessionRepository.findByPrincipalName(currentId);
        List<Session> sessions = new ArrayList<>();

        sessionMap.forEach((k, v) -> sessions.add(v));

        return sessions;
    }
}
