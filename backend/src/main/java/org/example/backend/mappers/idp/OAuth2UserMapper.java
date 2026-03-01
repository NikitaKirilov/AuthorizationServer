package org.example.backend.mappers.idp;

import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public abstract class OAuth2UserMapper {

    protected abstract User mapOAuth2UserToEntity(OAuth2User oauth2User);

    protected abstract String getAssociatedRegistrationId();
}
