package org.example.backend.mappers.users;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserMapper {

    User mapToUser(OAuth2UserRequest request, OAuth2User oAuth2User, ClientRegistrationWrapper wrapper);

    OAuth2ProviderType getProviderType();
}
