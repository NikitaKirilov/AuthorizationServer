package org.example.backend.models.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedUserToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;
    private final String userDeviceId;

    public AuthenticatedUserToken(
            UserPrincipal principal, String userDeviceId, Collection<GrantedAuthority> authorities
    ) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.userDeviceId = userDeviceId;
    }

    @Override
    public @NonNull Object getCredentials() {
        // Not storing credentials after user login
        return "";
    }

    @Override
    public @Nullable UserPrincipal getPrincipal() {
        return this.principal;
    }
}
