package org.example.backend.models;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class DefaultAuthenticationToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public DefaultAuthenticationToken(
            UserPrincipal principal,
            @Nullable Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
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

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }
}
