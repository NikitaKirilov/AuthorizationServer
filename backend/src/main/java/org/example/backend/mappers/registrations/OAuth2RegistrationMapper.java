package org.example.backend.mappers.registrations;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;

public abstract class OAuth2RegistrationMapper {

    private static final String DEFAULT_REDIRECT_URI = "{baseUrl}/{action}/oauth2/code/{registrationId}";

    final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientAuthenticationMethod(method)
                .redirectUri(DEFAULT_REDIRECT_URI)
                .authorizationGrantType(AUTHORIZATION_CODE);
    }

    public abstract ClientRegistration mapToClientRegistration(ClientRegistrationWrapper wrapper);

    public abstract OAuth2ProviderType getProviderType();
}
