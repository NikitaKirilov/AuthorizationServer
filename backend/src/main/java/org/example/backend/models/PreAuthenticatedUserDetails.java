package org.example.backend.models;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class PreAuthenticatedUserDetails implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;

    private String email;
    private String password;

    public PreAuthenticatedUserDetails(User user) {
        this.authorities = user.getGrantedAuthorities();

        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
