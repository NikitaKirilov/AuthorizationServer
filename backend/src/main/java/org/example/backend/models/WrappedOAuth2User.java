package org.example.backend.models;

import lombok.Getter;
import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
public class WrappedOAuth2User extends DefaultUserDetails implements OAuth2User {
    private final DefaultOAuth2User oAuth2User;

    public WrappedOAuth2User(DefaultOAuth2User oAuth2User, User user) {
        super(user);
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.oAuth2User.getAttributes();
    }

    @Override
    public String getName() {
        return this.oAuth2User.getName();
    }
}
