package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.PreAuthenticatedUserDetails;
import org.example.backend.services.UserAuthFlowService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends DefaultAuthenticationSuccessHandler {

    private final UserAuthFlowService userAuthFlowService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof PreAuthenticatedUserDetails details) {
            userAuthFlowService.processLogin(request, response, details);
        }

        super.onAuthenticationSuccess(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
}
