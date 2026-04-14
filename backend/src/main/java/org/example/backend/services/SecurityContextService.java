package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.UserPrincipal;
import org.example.backend.models.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextService {

    private final SecurityContextRepository securityContextRepository;

    public void updateSecurityContext(HttpServletRequest request, HttpServletResponse response, User user) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserPrincipal principal = new UserPrincipal(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);
    }
}
