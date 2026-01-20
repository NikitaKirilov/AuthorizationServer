package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.models.DefaultUserDetails;
import org.example.backend.models.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextService {

    private final SecurityContextRepository securityContextRepository;

    public void updateSecurityContext(HttpServletRequest request, HttpServletResponse response, User user) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = new DefaultUserDetails(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);

        securityContextRepository.saveContext(context, request, response);
    }

    public String getUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultUserDetails userDetails) {
            return userDetails.getId();
        }

        throw new RegistrationException("Cannot get user id from context");
    }
}
