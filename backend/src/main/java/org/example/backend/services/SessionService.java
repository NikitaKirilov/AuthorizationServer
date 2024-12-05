package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService<S extends Session> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final FindByIndexNameSessionRepository<S> findByIndexNameSessionRepository;

    //TODO: logoutIdpRegistrationUsers(IdpRegistration idpRegistration)
    //TODO: refreshUserSessions(String userId) -> updateRememberMeToken(User user)
}
