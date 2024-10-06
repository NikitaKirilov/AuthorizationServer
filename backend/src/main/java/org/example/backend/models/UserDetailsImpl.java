package org.example.backend.models;

import lombok.Getter;
import org.example.backend.models.entities.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final long id;

    private final List<SimpleGrantedAuthority> authorities;

    private final String email;
    private final boolean emailVerified;

    private final String password;

    private final String name;
    private final String familyName;

    private final Timestamp updatedAt;

    public UserDetailsImpl(User user) {
        this.id = user.getId();

        this.authorities = user.getScopes().stream()
                .map(scope -> new SimpleGrantedAuthority(scope.getName())).toList();

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.password = user.getPassword();

        this.name = user.getName();
        this.familyName = user.getFamilyName();

        this.updatedAt = user.getUpdatedAt();
    }


    @Override
    public String getUsername() {
        return this.email;
    }
}
