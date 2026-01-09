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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final String REGISTERED_USER_ID = "REGISTERED_USER_ID";
    private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    public String getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute(REGISTERED_USER_ID) == null) {
            throw new RegistrationException("Cannot create new token because of lost session");
        } else if (session.isNew() || session.getAttribute(SPRING_SECURITY_CONTEXT) == null) {
            SecurityContext context = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
            if (context.getAuthentication() instanceof CustomUserDetails userDetails) {
                return userDetails.getId();
            }
        }

        return session.getAttribute(REGISTERED_USER_ID).toString();
    }

    public void setSessionOnRegistration(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(REGISTERED_USER_ID, user.getId());
    }

    public void activateSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(
                new CustomUserDetails(user), null, user.getGrantedAuthorities()
        );

        securityContext.setAuthentication(auth);

        session.setAttribute(REGISTERED_USER_ID, null);
        session.setAttribute(SPRING_SECURITY_CONTEXT, securityContext);
    }
}
