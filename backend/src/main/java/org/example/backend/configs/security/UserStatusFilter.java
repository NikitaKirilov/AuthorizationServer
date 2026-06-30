package org.example.backend.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.example.backend.configs.SecurityConfig.PERMIT_ALL_LIST;

@Component
public class UserStatusFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            if (userPrincipal.isBlocked()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "USER_IS_BLOCKED");
                return;
            }

            if (!userPrincipal.isEmailVerified()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "EMAIL_NOT_VERIFIED");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PERMIT_ALL_LIST.stream()
                .anyMatch(matcher -> matcher.matches(request));
    }
}
