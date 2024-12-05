package org.example.backend.services;

import org.example.backend.configs.oauth2.OAuth2UserInfoRequestConverter;
import org.example.backend.mappers.users.OAuth2UserMapper;
import org.example.backend.mappers.users.OAuth2UserMapperFactory;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.entities.User;
import org.example.backend.models.enums.OAuth2ProviderType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class JPAOAuth2UserService extends DefaultOAuth2UserService {

    private final ClientRegistrationWrapperService clientRegistrationWrapperService;
    private final OAuth2UserMapperFactory oAuth2UserMapperFactory;
    private final UserService userService;

    public JPAOAuth2UserService(ClientRegistrationWrapperService clientRegistrationWrapperService,
                                OAuth2UserMapperFactory oAuth2UserMapperFactory,
                                OAuth2UserInfoRequestConverter converter,
                                UserService userService) {
        super.setRequestEntityConverter(converter);
        this.clientRegistrationWrapperService = clientRegistrationWrapperService;
        this.oAuth2UserMapperFactory = oAuth2UserMapperFactory;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ClientRegistrationWrapper clientRegistrationWrapper = clientRegistrationWrapperService.getByRegistrationId(registrationId);
        OAuth2ProviderType oAuth2ProviderType = clientRegistrationWrapper.getType();
        OAuth2UserMapper mapper = oAuth2UserMapperFactory.getMapper(oAuth2ProviderType);
        User user = mapper.mapToUser(userRequest, oAuth2User, clientRegistrationWrapper);

        return new CustomOAuth2User(oAuth2User.getAttributes(), userService.saveUserOnIdpLogin(user));
    }
}
