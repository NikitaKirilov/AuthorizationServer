package org.example.backend.mappers.oauth2user;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2UserMappers {

    private final Map<String, OAuth2UserMapper> mappersMap = new HashMap<>();

    public OAuth2UserMappers(List<OAuth2UserMapper> mappers) {
        mappers.forEach(mapper ->
                mappersMap.put(mapper.getAssociatedRegistrationId(), mapper));
    }

    public OAuth2UserMapper getMapper(String registrationId) {
        return mappersMap.get(registrationId);
    }
}
