package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.services.SessionService;
import org.springframework.security.core.Authentication;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final SessionService<? extends Session> sessionService;

    @GetMapping("/user/current")
    public Principal getUserPrincipal(Authentication principal) {
        return principal;
    }
}
