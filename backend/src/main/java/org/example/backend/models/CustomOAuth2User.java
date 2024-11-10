package org.example.backend.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.backend.models.entities.User;
import org.example.backend.utils.UserUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class CustomOAuth2User implements OAuth2User, Serializable {

    private final String id;
    private final String idpRegistrationId;

    private final String email;
    private final boolean emailVerified;

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    private final String password;

    private final String userName;
    private final String givenName;
    private final String familyName;

    private final Timestamp updatedAt;

    public CustomOAuth2User(Map<String, Object> attributes, User user) {
        this.id = user.getId();

        this.idpRegistrationId = user.getIdpRegistration().getId();

        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();

        this.authorities = UserUtils.getAuthorities(user);
        this.attributes = attributes;

        this.password = user.getPassword();

        this.userName = user.getGivenName();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();

        this.updatedAt = user.getUpdatedAt();
    }

    @Override
    public String getName() {
        return this.id;
    }
}
