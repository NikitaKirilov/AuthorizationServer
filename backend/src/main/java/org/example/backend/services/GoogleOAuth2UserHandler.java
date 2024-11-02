package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static org.example.backend.models.enums.OAuth2ProviderType.GOOGLE;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.EMAIL_VERIFIED;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.FAMILY_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.GIVEN_NAME;
import static org.springframework.security.oauth2.core.oidc.StandardClaimNames.NAME;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserHandler implements OAuth2UserHandler {

    private final UserService userService;

    //TODO rename this class to DefaultOAuth2UserHandler implement methods and use as superclass
    @Override
    public CustomOAuth2User handleUser(OAuth2UserRequest request, OAuth2User idpUser, IdpRegistration idpRegistration) {
        String email = idpUser.getAttribute(EMAIL);
        Optional<User> existingUser = userService.getOptionalByEmail(email);

        Timestamp currentTimestamp = Timestamp.from(Instant.now());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            user.setIdpRegistration(idpRegistration);
            user.setLastLogin(currentTimestamp);

            return new CustomOAuth2User(idpUser.getAttributes(), userService.save(user));
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .idpRegistration(idpRegistration)
                .email(email)
                .emailVerified(Objects.equals(TRUE, idpUser.getAttribute(EMAIL_VERIFIED)))
                .name(idpUser.getAttribute(NAME))
                .givenName(idpUser.getAttribute(GIVEN_NAME))
                .familyName(idpUser.getAttribute(FAMILY_NAME))
                .lastLogin(currentTimestamp)
                .createdAt(currentTimestamp)
                .updatedAt(currentTimestamp)
                .build();

        return new CustomOAuth2User(idpUser.getAttributes(), userService.save(user));
    }

    @Override
    public OAuth2ProviderType getHandlerType() {
        return GOOGLE;
    }
}
