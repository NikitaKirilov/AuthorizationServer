package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.backend.services.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SessionService sessionService;

    @GetMapping("/user/current")
    public Principal getUserPrincipal(HttpServletRequest request, Authentication principal) {
        request.getSession();
        return principal;
    }

    @GetMapping("/sessions")
    public List<Session> getCurrentUserSessions() {
        return sessionService.getUserSessions();
    }
}
