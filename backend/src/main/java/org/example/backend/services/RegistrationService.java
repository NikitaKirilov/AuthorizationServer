package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailTokenExpiredException;
import org.example.backend.exceptions.EmailTokenNotFoundException;
import org.example.backend.exceptions.EmailTokenTooManyAttemptsException;
import org.example.backend.exceptions.InvalidEmailVerificationCode;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.entities.EmailVerificationToken;
import org.example.backend.models.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    @Qualifier("defaultAuthenticationSuccessHandler")
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final EmailService emailservice;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final SessionService sessionService;
    private final UserService userService;

    @Transactional
    public void processRegistration(HttpServletRequest request, UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        String code = emailVerificationTokenService.createTokenAndGetCode(registeredUser);
        emailservice.sendCode(registeredUser, code);
        sessionService.updateSession(request, registeredUser);
    }

    @Transactional(noRollbackFor = {
            InvalidEmailVerificationCode.class,
            EmailTokenExpiredException.class,
            EmailTokenTooManyAttemptsException.class,
    })
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String code) {
        String userId = sessionService.getUserId(request);
        User user = userService.getById(userId);
        EmailVerificationToken token = user.getActiveEmailVerificationToken()
                .orElseThrow(() -> new EmailTokenNotFoundException("Email token not found"));

        emailVerificationTokenService.validateCode(code, token);
        userService.activateUser(user);
        sessionService.updateSession(request, user);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            throw new ServerError(e);
        }
    }

    @Transactional
    public void refreshCode(HttpServletRequest request) {
        String userId = sessionService.getUserId(request);
        User user = userService.getById(userId);
        userService.checkAndUpdateNextVerificationTokenAt(user);
        String code = emailVerificationTokenService.createTokenAndGetCode(user);
        emailservice.sendCode(user, code);
    }
}
