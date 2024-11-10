package org.example.backend.configs;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.idpregistrations.IdpRegistrationNotFoundException;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.example.backend.services.IdpRegistrationService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomClientRegistrationRepository implements ClientRegistrationRepository {

    private final IdpRegistrationService idpRegistrationService;

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Optional<IdpRegistration> optional = idpRegistrationService.getOptionalByRegistrationId(registrationId);

        if (optional.isEmpty()) {
            throw new IdpRegistrationNotFoundException(registrationId);
        }

        IdpRegistration idpRegistration = optional.get();
        OAuth2ProviderType type = idpRegistration.getType();

        return type.getBuilder(registrationId)
                .clientId(idpRegistration.getClientId())
                .clientSecret(idpRegistration.getClientSecret())
                .clientName(idpRegistration.getClientName())
                .build();
    }
}
