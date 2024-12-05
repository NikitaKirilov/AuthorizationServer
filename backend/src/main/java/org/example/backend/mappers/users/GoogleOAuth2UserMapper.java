package org.example.backend.mappers.users;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static java.lang.Boolean.TRUE;
import static java.util.UUID.randomUUID;
import static org.example.backend.models.enums.OAuth2ProviderType.GOOGLE;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;

@Service
public class GoogleOAuth2UserMapper implements OAuth2UserMapper {

    @Override
    public User mapToUser(OAuth2UserRequest request, OAuth2User oAuth2User, ClientRegistrationWrapper wrapper) {
        boolean emailVerified = TRUE.equals(oAuth2User.getAttribute(EMAIL_VERIFIED));
        Timestamp timestampNow = getCurrentTimestamp();

        return new User()
                .setId(randomUUID().toString())

                .setClientRegistrationWrapper(wrapper)

                .setEmail(oAuth2User.getAttribute(EMAIL))
                .setEmailVerified(emailVerified)

                .setName(oAuth2User.getAttribute(NAME))
                .setGivenName(oAuth2User.getAttribute(GIVEN_NAME))
                .setFamilyName(oAuth2User.getAttribute(FAMILY_NAME))

                .setLastLogin(timestampNow)
                .setCreatedAt(timestampNow)
                .setUpdatedAt(timestampNow);
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return GOOGLE;
    }
}
