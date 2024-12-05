package org.example.backend.configs.oauth2;

import lombok.RequiredArgsConstructor;
import org.example.backend.mappers.registrations.OAuth2RegistrationMapper;
import org.example.backend.mappers.registrations.OAuth2RegistrationMapperFactory;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.example.backend.services.ClientRegistrationWrapperService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JPAClientRegistrationRepository implements ClientRegistrationRepository {

    private final ClientRegistrationWrapperService clientRegistrationWrapperService;
    private final OAuth2RegistrationMapperFactory oAuth2RegistrationMapperFactory;

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        ClientRegistrationWrapper clientRegistrationWrapper =
                clientRegistrationWrapperService.getByRegistrationId(registrationId);

        return getClientRegistration(clientRegistrationWrapper);
    }

    private ClientRegistration getClientRegistration(ClientRegistrationWrapper clientRegistrationWrapper) {
        OAuth2ProviderType providerType = clientRegistrationWrapper.getType();
        OAuth2RegistrationMapper mapper = oAuth2RegistrationMapperFactory.getMapper(providerType);

        return mapper.mapToClientRegistration(clientRegistrationWrapper);
    }
}
