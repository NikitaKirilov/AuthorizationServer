package org.example.backend.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.RegistrationException;
import org.example.backend.models.CustomUserDetails;
import org.example.backend.models.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public String getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute(SPRING_SECURITY_CONTEXT) == null) {
            throw new RegistrationException("No user session. Try to re-login");
        }

        SecurityContext context = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        if (context.getAuthentication() instanceof UsernamePasswordAuthenticationToken token &&
                token.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }

        throw new RegistrationException("Cannot get user id for context: " + context);
    }

    public void updateSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);

        session.setAttribute(SPRING_SECURITY_CONTEXT, context);
    }
}
