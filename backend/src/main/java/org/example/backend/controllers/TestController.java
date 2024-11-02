package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SessionRegistry sessionRegistry;

    @GetMapping("/user/current")
    public Principal getUserPrincipal(Authentication principal) {
        return principal;
    }

    @GetMapping("/principals")
    public List<Object> getAllPrincipals() {
        return sessionRegistry.getAllPrincipals();
    }

    @GetMapping("/remove")
    public void removeCurrentSession(HttpServletRequest request) {
        sessionRegistry.removeSessionInformation(request.getSession().getId());
    }
}
