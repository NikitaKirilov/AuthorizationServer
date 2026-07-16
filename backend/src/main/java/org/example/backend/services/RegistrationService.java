package org.example.backend.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RegistrationDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.EmailVerificationCodeValidationException;
import org.example.backend.exceptions.ServerError;
import org.example.backend.models.entities.EmailVerificationCode;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.models.enums.CooldownAction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.core.Authentication;
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
    private final SecurityContextService securityContextService;
    private final SessionService sessionService;
    private final UserDeviceService userDeviceService;
    private final UserService userService;

    @Transactional
    public void processRegistration(
            HttpServletRequest request,
            HttpServletResponse response,
            RegistrationDto registrationDto
    ) {
        User user = userService.createUser(registrationDto);
        securityContextService.createRegisteredUserContext(request, response, user);
        if (!cooldownService.isBlocked(CooldownAction.NEW_CODE_REQUEST, user.getId())) {
            cooldownService.acquire(CooldownAction.NEW_CODE_REQUEST, user.getId());
            emailVerificationCodeService.sendCode(user);
        }
    }

    @Transactional
    @Retryable(value = DataIntegrityViolationException.class, delay = 10)
    public void createNewCode() {
        User user = userService.getCurrentUser();

        if (user.isEmailVerified()) {
            throw new EmailIsAlreadyTakenException();
        }

        cooldownService.acquire(CooldownAction.NEW_CODE_REQUEST, user.getId());
        emailVerificationCodeService.sendCode(user);
    }

    @Transactional(noRollbackFor = EmailVerificationCodeValidationException.class)
    public void verifyEmail(HttpServletRequest request, HttpServletResponse response, String sourceCode) {
        User user = userService.getCurrentUserWithAuthorities();
        EmailVerificationCode code = emailVerificationCodeService.getActiveByUser(user);

        emailVerificationCodeService.validateCode(sourceCode, code);
        userService.confirmEmail(user);
        sessionService.deleteUserSessionsExceptCurrent(user);

        UserDevice device = userDeviceService.saveAndVerifyDevice(user, request);
        Authentication authentication = securityContextService.createAuthenticatedUserContext(
                request, response, user, device
        );

        try {
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (ServletException | IOException e) {
            throw new ServerError(e);
        }
    }
}
