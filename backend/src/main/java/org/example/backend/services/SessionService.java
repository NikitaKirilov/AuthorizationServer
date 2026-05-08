package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.UserPrincipal;
import org.example.backend.models.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository.RedisSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    public void refreshUserSessions(User user) {
        Map<String, RedisSession> sessions = redisIndexedSessionRepository.findByPrincipalName(user.getId());
        sessions.forEach((sessionId, session) -> {
            SecurityContextImpl context = session.getAttribute(SPRING_SECURITY_CONTEXT);
            Authentication prevAuthentication = context.getAuthentication();

            UserPrincipal userPrincipal = new UserPrincipal(user);
            UsernamePasswordAuthenticationToken updatedAuthentication = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );
            updatedAuthentication.setDetails(prevAuthentication.getDetails());

            context.setAuthentication(updatedAuthentication);
            session.setAttribute(SPRING_SECURITY_CONTEXT, context);

            redisIndexedSessionRepository.save(session);
        });
    }

    public void closeUserSessionsExceptCurrent(User user) {
        String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        redisIndexedSessionRepository.findByPrincipalName(user.getId())
                .forEach((sessionId, session) -> {
                    if (!sessionId.equals(currentSessionId)) {
                        redisIndexedSessionRepository.deleteById(sessionId);
                    }
                });
    }
}
