package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.enums.AttemptAction;
import org.example.backend.models.security.PreAuthenticatedUserDetails;
import org.example.backend.services.AttemptService;
import org.example.backend.services.UserAuthService;
import org.example.backend.utils.RequestUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends DefaultAuthenticationSuccessHandler {

    private final AttemptService attemptService;
    private final UserAuthService userAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof PreAuthenticatedUserDetails details) {
            authentication = userAuthService.loginUser(request, response, details);
            attemptService.reset(AttemptAction.LOGIN, details.getUsername(), RequestUtils.getIpAddress(request));
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
