package org.example.backend.models.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrincipal implements AuthenticatedPrincipal, Serializable {

    private String id;

    private Collection<ResourceBasedGrantedAuthority> authorities;

    private String email;
    private boolean emailVerified;

    private String nickname;
    private String givenName;
    private String familyName;

    private LocalDate birthday;

    @JsonCreator
    public UserPrincipal() {
    }

    public UserPrincipal(User user) {
        this.id = user.getId();

        this.authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(authority ->
                        new ResourceBasedGrantedAuthority(authority.getResource(), authority.getName()))
                .collect(Collectors.toSet());

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.nickname = user.getNickname();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();

        this.birthday = user.getBirthday();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
