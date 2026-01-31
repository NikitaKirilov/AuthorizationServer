package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
public class UserPrincipal implements AuthenticatedPrincipal, Serializable {

    private String id;

    private Collection<? extends GrantedAuthority> authorities;

    private String email;
    private boolean emailVerified;

    private String name;
    private String givenName;
    private String familyName;

    @JsonCreator
    public UserPrincipal() {
    }

    public UserPrincipal(User user) {
        this.id = user.getId();

        this.authorities = user.getGrantedAuthorities();

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.name = user.getName();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
    }

    @Override
    public String getName() {
        return this.id;
    }

    public String getUsername() {
        return this.name;
    }
}
