package org.example.backend.models.security;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
public class PreAuthenticatedUserDetails implements UserDetails {

    private String email;
    private String password;

    public PreAuthenticatedUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // not storing any authorities because this is temporary object and will be replaced with UserPrincipal after authentication success
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
