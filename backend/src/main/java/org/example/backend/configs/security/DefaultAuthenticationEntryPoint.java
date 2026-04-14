package org.example.backend.configs.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/app")) {
            requestCache.saveRequest(request, response);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "LOGIN_REQUIRED");
    }
}
