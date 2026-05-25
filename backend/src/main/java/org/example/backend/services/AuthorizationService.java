package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.SecurityContextException;
import org.example.backend.mappers.OAuth2AuthorizationMapper;
import org.example.backend.models.entities.Authorization;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.models.security.AuthenticatedUserToken;
import org.example.backend.models.security.UserDeviceInfo;
import org.example.backend.models.security.UserPrincipal;
import org.example.backend.repositories.AuthorizationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationService implements OAuth2AuthorizationService {

    private final AuthorizationRepository authorizationRepository;
    private final OAuth2AuthorizationMapper oAuth2AuthorizationMapper;

    @Override
    public OAuth2Authorization findById(String id) {
        return authorizationRepository.findById(id)
                .map(oAuth2AuthorizationMapper::toObject)
                .orElse(null);
    }

    public List<Authorization> getAuthorizationsByUserId(String userId) {
        return authorizationRepository.findAllByPrincipalName(userId);
    }

    public Map<String, Integer> countAuthorizationsByDevice(User user) {
        Map<String, Integer> authorizationsCount = new HashMap<>();
        getAuthorizationsByUserId(user.getId())
                .forEach(authorization -> {
                    OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationMapper.toObject(authorization);
                    UserDeviceInfo userDeviceInfo = extractUserAuthentication(oAuth2Authorization).getUserDeviceInfo();
                    authorizationsCount.merge(userDeviceInfo.getId(), 1, Integer::sum);
                });

        return authorizationsCount;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        this.authorizationRepository.save(oAuth2AuthorizationMapper.toEntity(authorization));
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Optional<Authorization> result;

        if (tokenType == null) {
            result = authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByAuthorizationCodeValue(token);
        } else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByAccessTokenValue(token);
        } else if (OAuth2ParameterNames.REFRESH_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByRefreshTokenValue(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            result = authorizationRepository.findByOidcIdTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            result = authorizationRepository.findByDeviceCodeValue(token);
        } else {
            result = Optional.empty();
        }

        return result.map(oAuth2AuthorizationMapper::toObject).orElse(null);
    }

    public void updateUserAuthorizations(User user) {
        getAuthorizationsByUserId(user.getId())
                .forEach(authorization -> {
                    OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationMapper.toObject(authorization);
                    AuthenticatedUserToken authentication = extractUserAuthentication(oAuth2Authorization);

                    AuthenticatedUserToken newAuthentication = new AuthenticatedUserToken(
                            new UserPrincipal(user), authentication.getUserDeviceInfo()
                    );
                    oAuth2Authorization.getAttributes().put(Principal.class.getName(), newAuthentication);

                    Authorization updatedAuthorization = oAuth2AuthorizationMapper.toEntity(oAuth2Authorization);
                    authorizationRepository.save(updatedAuthorization);
                });
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        this.authorizationRepository.deleteById(authorization.getId());
    }

    public void deleteAuthorizationsByUserAndDevice(User user, UserDevice userDevice) {
        getAuthorizationsByUserId(user.getId())
                .forEach(authorization -> {
                    OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationMapper.toObject(authorization);
                    UserDeviceInfo userDeviceInfo = extractUserAuthentication(oAuth2Authorization).getUserDeviceInfo();
                    if (Objects.equals(userDeviceInfo.getId(), userDevice.getId())) {
                        authorizationRepository.delete(authorization);
                    }
                });
    }

    public void deleteAllByUserId(String userId) {
        this.authorizationRepository.deleteAllByPrincipalName(userId);
    }

    private AuthenticatedUserToken extractUserAuthentication(OAuth2Authorization oAuth2Authorization) {
        Map<String, Object> attributes = oAuth2Authorization.getAttributes();
        Principal authentication = (Principal) attributes.get(Principal.class.getName());

        if (authentication instanceof AuthenticatedUserToken token) {
            return token;
        }

        throw new SecurityContextException("Unsupported authentication token");
    }
}
