package org.example.backend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
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
            HttpServletResponse response,
            @RequestBody RegistrationDto registrationDto
    ) {
        registrationService.processRegistration(request, response, registrationDto);
    }

    @PostMapping("/code/new")
    public void createNewCode() {
        registrationService.createNewCode();
    }

    @PutMapping("/verify")
    public void verifyEmail(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("sourceCode") String sourceCode
    ) {
        registrationService.verifyEmail(request, response, sourceCode);
    }
}
