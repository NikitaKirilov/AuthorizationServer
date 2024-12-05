package org.example.backend.mappers.users;

import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2UserMapperFactory {

    private final Map<OAuth2ProviderType, OAuth2UserMapper> mappers = new EnumMap<>(OAuth2ProviderType.class);

    public OAuth2UserMapperFactory(List<OAuth2UserMapper> mappersList) {
        mappersList.forEach(mapper -> mappers.put(mapper.getProviderType(), mapper));
    }

    public OAuth2UserMapper getMapper(OAuth2ProviderType type) {
        return mappers.get(type);
    }
}
