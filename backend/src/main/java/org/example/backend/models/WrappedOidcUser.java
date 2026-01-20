package org.example.backend.models;

import lombok.Getter;
import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Map;

@Getter
public class WrappedOidcUser extends DefaultUserDetails implements OidcUser {
    private final DefaultOidcUser oidcUser;

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

    @Override
    public String getName() {
        return this.oidcUser.getName();
    }
}
