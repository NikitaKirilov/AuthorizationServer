package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.utils.SecurityUtils;
import org.example.backend.utils.UserUtils;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository.RedisSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Objects;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    public Map<String, RedisSession> getSessionsByUserId(String userId) {
        return redisIndexedSessionRepository.findByPrincipalName(userId);
    }

    public void updateUserAuthorities(User user) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    SecurityContextImpl context = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    AuthenticatedUserToken authentication = SecurityUtils.getAuthenticatedUserToken(context);

                    AuthenticatedUserToken newAuthentication = new AuthenticatedUserToken(
                            authentication.getPrincipal(),
                            authentication.getUserDeviceId(),
                            UserUtils.getGrantedAuthorities(user)
                    );
                    newAuthentication.setDetails(authentication.getDetails());

                    context.setAuthentication(newAuthentication);

                    redisIndexedSessionRepository.save(session);
                });
    }

    public void deleteUserSessionsExceptCurrent(User user) {
        String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    if (!sessionId.equals(currentSessionId)) {
                        redisIndexedSessionRepository.deleteById(sessionId);
                    }
                });
    }

    public void deleteUserSessions(User user) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> redisIndexedSessionRepository.deleteById(sessionId));
    }

    public void deleteSessionsByUserAndDevice(User user, UserDevice userDevice) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    SecurityContextImpl context = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    AuthenticatedUserToken authentication = SecurityUtils.getAuthenticatedUserToken(context);
                    String userDeviceId = authentication.getUserDeviceId();

                    if (Objects.equals(userDeviceId, userDevice.getId())) {
                        redisIndexedSessionRepository.deleteById(sessionId);
                    }
                });
    }
}
