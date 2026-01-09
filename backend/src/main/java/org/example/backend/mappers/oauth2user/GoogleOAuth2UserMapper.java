package org.example.backend.mappers.oauth2user;

import org.example.backend.models.DefaultClaimNames;
import org.example.backend.models.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuth2UserMapper implements OAuth2UserMapper {

    private static final String REGISTRATION_ID = "google";

    @Override
    public User mapOAuth2UserToEntity(OAuth2User oauth2User) {
        User user = new User();

        user.setEmail(oauth2User.getAttribute(DefaultClaimNames.EMAIL.getValue()));
        user.setEmailVerified(oauth2User.getAttribute(DefaultClaimNames.EMAIL_VERIFIED.getValue()));
        user.setName(oauth2User.getAttribute(DefaultClaimNames.NAME.getValue()));
        user.setGivenName(oauth2User.getAttribute(DefaultClaimNames.GIVEN_NAME.getValue()));
        user.setFamilyName(oauth2User.getAttribute(DefaultClaimNames.FAMILY_NAME.getValue()));

        return user;
    }

    @Override
    public String getAssociatedRegistrationId() {
        return REGISTRATION_ID;
    }
}
