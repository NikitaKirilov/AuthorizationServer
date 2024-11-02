package org.example.backend.services;

import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserHandler {

    CustomOAuth2User handleUser(OAuth2UserRequest request, OAuth2User user, IdpRegistration idpRegistration);

    OAuth2ProviderType getHandlerType();
}
