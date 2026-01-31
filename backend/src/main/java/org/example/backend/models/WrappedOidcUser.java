package org.example.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;

@Getter
@Setter
public class WrappedOidcUser extends UserPrincipal implements OidcUser {

    private final DefaultOidcUser oidcUser;

    @JsonCreator
    public WrappedOidcUser(DefaultOidcUser oidcUser) {
        this.oidcUser = oidcUser;
    }

    public WrappedOidcUser(DefaultOidcUser oidcUser, User user) {
        super(user);
        this.oidcUser = oidcUser;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.oidcUser.getAttributes();
    }
}
