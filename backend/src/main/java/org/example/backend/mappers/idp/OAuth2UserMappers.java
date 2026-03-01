package org.example.backend.mappers.idp;

import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    public User delegate(OAuth2User oAuth2User, String registrationId) {
        OAuth2UserMapper mapper = mappersMap.get(registrationId);
        return mapper.mapOAuth2UserToEntity(oAuth2User);
    }
}
