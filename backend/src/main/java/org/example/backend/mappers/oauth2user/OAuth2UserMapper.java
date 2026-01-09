package org.example.backend.mappers.oauth2user;

import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserMapper {

    User mapOAuth2UserToEntity(OAuth2User oauth2User);

    String getAssociatedRegistrationId();
}
