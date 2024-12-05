package org.example.backend.mappers.registrations;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import static org.example.backend.models.enums.OAuth2ProviderType.GOOGLE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.oidc.IdTokenClaimNames.SUB;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;

@Component
public class GoogleOAuth2RegistrationMapper extends OAuth2RegistrationMapper {

    @Override
    public ClientRegistration mapToClientRegistration(ClientRegistrationWrapper wrapper) {
        return getBuilder(wrapper.getRegistrationId(), CLIENT_SECRET_BASIC)
                .clientId(wrapper.getClientId())
                .clientSecret(wrapper.getClientSecret())
                .clientName(wrapper.getClientName())

                .scope(PROFILE, EMAIL)

                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .issuerUri("https://accounts.google.com")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")

                .userNameAttributeName(SUB)
                .build();
    }

    @Override
    public OAuth2ProviderType getProviderType() {
        return GOOGLE;
    }
}
