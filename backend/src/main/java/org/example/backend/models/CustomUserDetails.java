package org.example.backend.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.backend.models.entities.User;
import org.example.backend.utils.UserUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

    private final String id;

    private final Collection<? extends GrantedAuthority> authorities;

    private final String email;
    private final boolean emailVerified;

    private final String password;

    private final String name;
    private final String givenName;
    private final String familyName;

    private final Timestamp updatedAt;

    public CustomUserDetails(User user) {
        this.id = user.getId();

        this.authorities = UserUtils.getAuthorities(user);

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.password = user.getPassword();

        this.name = user.getGivenName();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();

        this.updatedAt = user.getUpdatedAt();
    }

    @Override
    public String getUsername() {
        return this.id;
    }
}
