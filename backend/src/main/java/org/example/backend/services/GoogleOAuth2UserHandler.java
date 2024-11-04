package org.example.backend.services;

import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.example.backend.models.enums.OAuth2ProviderType.GOOGLE;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;

@Service
public class GoogleOAuth2UserHandler implements OAuth2UserHandler {

    @Override
    public User getUser(OAuth2UserRequest request, OAuth2User idpUser, IdpRegistration idpRegistration) {
        boolean emailVerified = TRUE.equals(idpUser.getAttribute(EMAIL_VERIFIED));
        Timestamp timestampNow = getCurrentTimestamp();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(idpUser.getAttribute(EMAIL))
                .emailVerified(emailVerified)
                .name(idpUser.getAttribute(NAME))
                .givenName(idpUser.getAttribute(GIVEN_NAME))
                .familyName(idpUser.getAttribute(FAMILY_NAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return GOOGLE;
    }
}
