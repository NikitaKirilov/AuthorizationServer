package org.example.backend.models.security;

import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
public class RegistrationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public RegistrationToken(UserPrincipal principal) {
        super(Collections.emptyList());
        super.setAuthenticated(true);

        this.principal = principal;
    }

    @Override
    public @Nullable Object getCredentials() {
        return "";
    }

    @Override
    public @Nullable Object getPrincipal() {
        return principal;
    }
}
