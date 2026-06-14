package org.example.backend.models.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedUserToken extends AbstractAuthenticationToken {

    private static final String AUTHORIZATION_SERVER = "AS";
    private final UserPrincipal principal;
    private final UserDeviceInfo userDeviceInfo;

    public AuthenticatedUserToken(
            UserPrincipal principal, UserDeviceInfo userDeviceInfo
    ) {
        super(principal.getAuthorities().stream()
                .filter(authority -> authority.getResource().equals(AUTHORIZATION_SERVER))
                .collect(Collectors.toSet())
        );
        super.setAuthenticated(true);

        this.principal = principal;
        this.userDeviceInfo = userDeviceInfo;
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
