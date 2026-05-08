package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.models.AuthenticatedUserAuthenticationToken;
import org.example.backend.models.UserPrincipal;
import org.example.backend.models.entities.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextService {

    private final SecurityContextRepository securityContextRepository;

    public Authentication createAuthorizedUserContext(HttpServletRequest request, HttpServletResponse response, User user) {
        SecurityContext context = SecurityContextHolder.getContext();

        UserPrincipal principal = new UserPrincipal(user);
        Object details = new WebAuthenticationDetails(request);
        AbstractAuthenticationToken authentication = new AuthenticatedUserAuthenticationToken(principal);

        authentication.setDetails(details);

        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);

        return authentication;
    }
}
