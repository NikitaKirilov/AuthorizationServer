package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.Action;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.resilience.annotation.Retryable;
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
    private final CooldownService cooldownService;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final UserService userService;

    @Transactional
    public void processRegistration(
            HttpServletRequest request,
            HttpServletResponse response,
            RegistrationDto registrationDto
    ) {
        User user = userService.registerUser(request, response, registrationDto);
        if (!cooldownService.isBlocked(Action.CODE_REQUEST, user.getId())) {
            emailVerificationCodeService.sendCode(user);
        }
    }

    @Transactional
    @Retryable(value = DataIntegrityViolationException.class, delay = 10)
    public void refreshCode() {
        User user = userService.getUserFromContext();
        if (user.isEmailVerified()) {
            throw new RegistrationException("User is already verified");
        }
        emailVerificationCodeService.sendCode(user);
    }

    @Transactional(noRollbackFor = EmailVerificationCodeValidationException.class)
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String code) {
        User user = userService.getUserFromContext();
        EmailVerificationCode token = emailVerificationCodeService.getActiveByUser(user);

        emailVerificationCodeService.validateCode(code, token);
        userService.activateUser(request, response, user);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            throw new ServerError(e);
        }
    }
}
