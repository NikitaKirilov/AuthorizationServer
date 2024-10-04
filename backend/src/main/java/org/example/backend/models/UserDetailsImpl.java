package org.example.backend.models;

import org.example.backend.models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final List<? extends GrantedAuthority> authorities;

    private final String username;
    private final String password;

    public UserDetailsImpl(User user) {
        this.authorities = user.getScopes().stream()
                .map(scope -> new SimpleGrantedAuthority(scope.getName())).toList();

        this.username = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
