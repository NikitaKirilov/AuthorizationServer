package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.exceptions.SecurityContextException;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public String getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }

        throw new SecurityContextException("Cannot get user id from context. Try to re login");
    }

    public AuthenticatedUserToken getAuthenticatedUserToken() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication instanceof AuthenticatedUserToken token) {
            return token;
        }

        throw new SecurityContextException("Cannot get AuthenticatedUserToken from context.");
    }
}
