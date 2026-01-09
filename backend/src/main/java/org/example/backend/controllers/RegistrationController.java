package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.services.RegistrationService;
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

    private final RegistrationService registrationService;

    @PostMapping
    public void registerUser(
            HttpServletRequest request,
            @RequestBody UserDto userDto
    ) {
        registrationService.processRegistration(request, userDto);
    }

    @PutMapping("/confirm")
    public void confirmEmail(HttpServletRequest request, HttpServletResponse response, @RequestParam String token) {
        registrationService.verifyEmail(request, response, token);
    }

    @PutMapping("/resend")
    public void resendToken(HttpServletRequest request) {
        registrationService.sendNewToken(request);
    }
}
