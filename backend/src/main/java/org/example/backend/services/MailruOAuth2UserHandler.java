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
import static org.example.backend.models.OAuth2UserAttributes.FIRST_NAME;
import static org.example.backend.models.OAuth2UserAttributes.LAST_NAME;
import static org.example.backend.models.enums.OAuth2ProviderType.MAILRU;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;

@Service
public class MailruOAuth2UserHandler implements OAuth2UserHandler {

    @Override
    public User getUser(OAuth2UserRequest request, OAuth2User oAuth2User, IdpRegistration idpRegistration) {
        Timestamp timestampNow = getCurrentTimestamp();

        return User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(oAuth2User.getAttribute(EMAIL))
                .emailVerified(TRUE)
                .name(oAuth2User.getAttribute(NAME))
                .givenName(oAuth2User.getAttribute(FIRST_NAME))
                .familyName(oAuth2User.getAttribute(LAST_NAME))
                .lastLogin(timestampNow)
                .createdAt(timestampNow)
                .updatedAt(timestampNow)
                .build();
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return MAILRU;
    }
}
