package org.example.backend.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.AttemptAction;
import org.example.backend.services.AttemptsService;
import org.example.backend.utils.RequestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.backend.configs.SecurityConfig.LOGIN_PROCESSING_URL;

@Component
@RequiredArgsConstructor
public class LoginAttemptsFilter extends OncePerRequestFilter {

    private final AttemptsService attemptService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String username = request.getParameter("username");
        String ip = RequestUtils.getIpAddress(request);

        if (attemptService.isBlocked(AttemptAction.LOGIN, username, ip)) {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many failed attempts");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return !LOGIN_PROCESSING_URL.equals(uri) || !HttpMethod.POST.name().equalsIgnoreCase(method);
    }
}
