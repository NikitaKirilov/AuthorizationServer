package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.Action;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserAuthFlowService {

    @Qualifier("defaultAuthenticationSuccessHandler")
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final CooldownService cooldownService;
    private final EmailService emailservice;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final SecurityContextService securityContextService;
    private final UserService userService;

    @Transactional
    public void processRegistration(HttpServletRequest request, HttpServletResponse response, UserDto userDto) {
        User registeredUser = userService.registerUser(userDto);
        securityContextService.updateSecurityContext(request, response, registeredUser);
    }

    @Transactional
    public void processLogin(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
        User user = userService.getByEmail(userDetails.getUsername());
        //TODO: add device service
        user.setLastLogin(Instant.now());
        securityContextService.updateSecurityContext(request, response, user);
    }

    @Transactional(noRollbackFor = EmailVerificationCodeValidationException.class)
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String code) {
        String userId = securityContextService.getUserId();
        User user = userService.getById(userId);
        EmailVerificationCode token = emailVerificationCodeService.getActiveByUser(user);

        emailVerificationCodeService.validateCode(code, token);
        userService.activateUser(user);
        securityContextService.updateSecurityContext(request, response, user);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            throw new ServerError(e);
        }
    }

    @Transactional
    @Retryable(value = DataIntegrityViolationException.class, delay = 10)
    public void createToken() {
        String userId = securityContextService.getUserId();
        User user = userService.getById(userId);

        cooldownService.acquire(Action.CODE_REQUEST, user.getId());

        String code = emailVerificationCodeService.createAndGetSourceCode(user);
        emailservice.sendEmailVerificationCode(user, code);
    }
}
