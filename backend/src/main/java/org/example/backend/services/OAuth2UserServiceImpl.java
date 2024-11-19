package org.example.backend.services;

import org.example.backend.configs.security.OAuth2UserInfoRequestConverter;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final IdpRegistrationService idpRegistrationService;
    private final OAuth2UserHandlerFactory oAuth2UserHandlerFactory;
    private final UserService userService;

    public OAuth2UserServiceImpl(IdpRegistrationService idpRegistrationService,
                                 OAuth2UserHandlerFactory oAuth2UserHandlerFactory,
                                 OAuth2UserInfoRequestConverter converter,
                                 UserService userService) {
        super.setRequestEntityConverter(converter);
        this.idpRegistrationService = idpRegistrationService;
        this.oAuth2UserHandlerFactory = oAuth2UserHandlerFactory;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User idpUser = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        IdpRegistration idpRegistration = idpRegistrationService.getByRegistrationId(registrationId);
        OAuth2ProviderType oAuth2ProviderType = idpRegistration.getType();

        OAuth2UserHandler handler = oAuth2UserHandlerFactory.getHandler(oAuth2ProviderType);
        User user = handler.getUser(userRequest, idpUser, idpRegistration);

        return new CustomOAuth2User(idpUser.getAttributes(), userService.saveUserOnIdpLogin(user));
    }
}
