package org.example.backend.services;

import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserHandler {

    User getUser(OAuth2UserRequest request, OAuth2User idpUser, IdpRegistration idpRegistration);

    OAuth2ProviderType getHandlerType();
}
