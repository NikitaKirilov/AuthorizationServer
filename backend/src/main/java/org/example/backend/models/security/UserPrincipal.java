package org.example.backend.models.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.io.Serializable;

@Getter
@Setter
public class UserPrincipal implements AuthenticatedPrincipal, Serializable {

    private String id;

    private boolean blocked;
    private boolean emailVerified;

    @JsonCreator
    public UserPrincipal() {
    }

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.blocked = user.isBlocked();
        this.emailVerified = user.isEmailVerified();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
