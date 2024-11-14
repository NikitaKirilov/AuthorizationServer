package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService<S extends Session> {

    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final FindByIndexNameSessionRepository<S> findByIndexNameSessionRepository;
}
