package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.CooldownAction;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.example.backend.utils.SecurityUtils;
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
    private final AuthorityService authorityService;
    private final CooldownService cooldownService;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final SecurityContextService securityContextService;
    private final UserService userService;

    @Transactional
    public void processRegistration(
            HttpServletRequest request,
            HttpServletResponse response,
            RegistrationDto registrationDto
    ) {
        User user = userService.createUser(registrationDto);
        authorityService.assignDefaultAuthority(user);
        securityContextService.updateSecurityContext(request, response, user);
        if (!cooldownService.isBlocked(CooldownAction.REQUEST_CODE, user.getId())) {
            cooldownService.acquire(CooldownAction.REQUEST_CODE, user.getId());
            emailVerificationCodeService.sendCode(user);
        }
    }

    @Transactional
    @Retryable(value = DataIntegrityViolationException.class, delay = 10)
    public void refreshCode() {
        User user = userService.getUserById(SecurityUtils.getCurrentUserId());

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        cooldownService.acquire(CooldownAction.REQUEST_CODE, user.getId());
        emailVerificationCodeService.sendCode(user);
    }

    @Transactional(noRollbackFor = EmailVerificationCodeValidationException.class)
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String srcCode) {
        User user = userService.getUserById(SecurityUtils.getCurrentUserId());
        EmailVerificationCode code = emailVerificationCodeService.getActiveByUser(user);

        emailVerificationCodeService.validateCode(srcCode, code);
        userService.updateEmail(user);
        securityContextService.updateSecurityContext(request, response, user);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException | ServletException e) {
            throw new ServerError(e);
        }
    }
}
