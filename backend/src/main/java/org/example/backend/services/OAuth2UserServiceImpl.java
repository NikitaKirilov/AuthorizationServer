package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final IdpRegistrationService idpRegistrationService;
    private final OAuth2UserHandlerFactory oAuth2UserHandlerFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        IdpRegistration idpRegistration = idpRegistrationService.getByRegistrationId(registrationId);
        OAuth2ProviderType oAuth2ProviderType = idpRegistration.getType();

        OAuth2UserHandler handler = oAuth2UserHandlerFactory.getHandler(oAuth2ProviderType);

        return handler.handleUser(userRequest, oAuth2User, idpRegistration);
    }
}
