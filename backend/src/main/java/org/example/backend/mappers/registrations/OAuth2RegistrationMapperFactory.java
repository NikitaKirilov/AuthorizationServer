package org.example.backend.mappers.registrations;

import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2RegistrationMapperFactory {

    private final Map<OAuth2ProviderType, OAuth2RegistrationMapper> mappers = new EnumMap<>(OAuth2ProviderType.class);

    public OAuth2RegistrationMapperFactory(List<OAuth2RegistrationMapper> mappersList) {
        mappersList.forEach(mapper -> mappers.put(mapper.getProviderType(), mapper));
    }

    public OAuth2RegistrationMapper getMapper(OAuth2ProviderType providerType) {
        return mappers.get(providerType);
    }
}
