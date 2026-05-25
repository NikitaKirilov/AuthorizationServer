package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final String REDIRECT_HEADER = "redirect";
    private static final String DEFAULT_TARGET_URL = "/app/login";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) throws IOException, ServletException {
        response.setHeader(REDIRECT_HEADER, DEFAULT_TARGET_URL);
    }
}
