package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.Authority;
import org.example.backend.models.entities.User;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrincipal implements AuthenticatedPrincipal, Serializable {

    private String id;

    private Collection<? extends GrantedAuthority> authorities;

    private String email;
    private boolean emailVerified;

    private String nickname;
    private String givenName;
    private String familyName;

    @JsonCreator
    public UserPrincipal() {
    }

    public UserPrincipal(User user) {
        this.id = user.getId();

        this.authorities = user.getAuthorities().stream()
                .map(Authority::toGrantedAuthority).collect(Collectors.toSet());

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.nickname = user.getNickname();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
