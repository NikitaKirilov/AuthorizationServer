package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.SecurityContextException;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.models.security.UserDeviceInfo;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository.RedisSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
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

    public Map<String, Integer> countSessionsByDevice(User user) {
        Map<String, Integer> sessionsCount = new HashMap<>();
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    AuthenticatedUserToken authentication = getAuthenticatedUserToken(session);
                    String deviceId = authentication.getUserDeviceInfo().getId();
                    sessionsCount.merge(deviceId, 1, Integer::sum);
                });

        return sessionsCount;
    }

    public void updateUserSessions(User user) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    SecurityContextImpl context = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                    Authentication authentication = context.getAuthentication();

                    if (authentication instanceof AuthenticatedUserToken prevAuthentication) {
                        UserPrincipal userPrincipal = new UserPrincipal(user);
                        UserDeviceInfo userDeviceInfo = prevAuthentication.getUserDeviceInfo();
                        AuthenticatedUserToken updatedAuthentication =
                                new AuthenticatedUserToken(userPrincipal, userDeviceInfo);
                        updatedAuthentication.setDetails(prevAuthentication.getDetails());

                        context.setAuthentication(updatedAuthentication);
                        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

                        redisIndexedSessionRepository.save(session);
                    }
        });
    }

    public void closeUserSessionsExceptCurrent(User user) {
        String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    if (!sessionId.equals(currentSessionId)) {
                        redisIndexedSessionRepository.deleteById(sessionId);
                    }
                });
    }

    public void closeUserSessions(User user) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> redisIndexedSessionRepository.deleteById(sessionId));
    }

    public void deleteSessionsByUserAndDevice(User user, UserDevice userDevice) {
        getSessionsByUserId(user.getId())
                .forEach((sessionId, session) -> {
                    AuthenticatedUserToken authentication = getAuthenticatedUserToken(session);
                    String userDeviceId = authentication.getUserDeviceInfo().getId();

                    if (Objects.equals(userDeviceId, userDevice.getId())) {
                        redisIndexedSessionRepository.deleteById(sessionId);
                    }
                });
    }

    private AuthenticatedUserToken getAuthenticatedUserToken(RedisSession session) {
        SecurityContextImpl context = session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication = context.getAuthentication();

        if (authentication instanceof AuthenticatedUserToken token) {
            return token;
        }

        throw new SecurityContextException("Unsupported authentication type");
    }
}
