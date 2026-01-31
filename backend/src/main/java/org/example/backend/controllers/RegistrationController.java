package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.services.UserAuthFlowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserAuthFlowService userAuthFlowService;

    @PostMapping
    public void registerUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody UserDto userDto
    ) {
        userAuthFlowService.processRegistration(request, response, userDto);
    }

    @PostMapping("/token")
    public void createToken() {
        userAuthFlowService.createToken();
    }

    @PutMapping("/confirm")
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, @RequestParam String code) {
        userAuthFlowService.verifyEmail(request, response, code);
    }
}
