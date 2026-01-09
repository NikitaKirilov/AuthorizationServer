package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final EmailService emailservice;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final SessionService sessionService;
    private final UserService userService;

    @Transactional
    public void processRegistration(HttpServletRequest request, UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        EmailVerificationToken token = emailVerificationTokenService.createToken(registeredUser);
        emailservice.sendToken(token);
        sessionService.setSessionOnRegistration(request, registeredUser);
    }

    @Transactional
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String tokenId) {
        EmailVerificationToken token = emailVerificationTokenService.getById(tokenId);
        emailVerificationTokenService.validateToken(token);

        User user = token.getUser();
        userService.activateUser(user);

        sessionService.activateSession(request, user);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            throw new ServerError(e);
        }
    }

    @Transactional
    public void sendNewToken(HttpServletRequest request) {
        String userId = sessionService.getUserId(request);
        User user = userService.getById(userId);
        EmailVerificationToken token = emailVerificationTokenService.createToken(user);
        emailservice.sendToken(token);
    }
}
