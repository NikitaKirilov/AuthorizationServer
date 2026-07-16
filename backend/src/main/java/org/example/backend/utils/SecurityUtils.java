package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.exceptions.SecurityContextException;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.models.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class SecurityUtils {

    public String getCurrentUserId() {
        return Optional.ofNullable(getCurrentAuthenticatedUserToken().getPrincipal())
                .map(UserPrincipal::getId)
                .orElseThrow(() -> new SecurityContextException("Cannot get user id from context. Try to re login"));
    }

    public String getCurrentDeviceId() {
        return Optional.ofNullable(getCurrentAuthenticatedUserToken().getUserDeviceId())
                .orElseThrow(() -> new SecurityContextException("Cannot get user device id from context. Try to re login"));
    }

    public AuthenticatedUserToken getCurrentAuthenticatedUserToken() {
        return getAuthenticatedUserToken(SecurityContextHolder.getContext());
    }

    public AuthenticatedUserToken getAuthenticatedUserToken(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof AuthenticatedUserToken token) {
            return token;
        }

        throw new SecurityContextException("Cannot get AuthenticatedUserToken from context.");
    }
}
