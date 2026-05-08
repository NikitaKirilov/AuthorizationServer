package org.example.backend.models;

import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
public class AuthenticatedUserAuthenticationToken extends AbstractAuthenticationToken {

    private static final String AUTHORIZATION_SERVER = "AS";
    private final UserPrincipal principal;

    public AuthenticatedUserAuthenticationToken(
            UserPrincipal principal
    ) {
        super(principal.getAuthorities().stream()
                .filter(authority -> authority.getResource().equals(AUTHORIZATION_SERVER))
                .collect(Collectors.toSet())
        );
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        // Not storing credentials after user login
        return "";
    }

    @Override
    public @Nullable UserPrincipal getPrincipal() {
        return principal;
    }
}
