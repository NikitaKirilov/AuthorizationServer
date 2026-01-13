package org.example.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrf")
public class CsrfController {

    /**
     * This endpoint exists to trigger CSRF token generation.
     * <p>
     * When a GET request is made, Spring Security automatically generates
     * and stores the CSRF token (e.g. in a cookie or HTTP session),
     * allowing the client to retrieve it for subsequent protected requests.
     */
    @GetMapping
    public void csrf() {
    }
}
