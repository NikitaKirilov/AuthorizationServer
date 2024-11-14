package org.example.backend.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.example.backend.utils.UserUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

    private String id;

    private Collection<? extends GrantedAuthority> authorities;

    private String email;
    private boolean emailVerified;

    private String password;

    private String name;
    private String givenName;
    private String familyName;

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
        return this.email;
    }
}
