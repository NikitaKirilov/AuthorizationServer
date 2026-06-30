package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorizationDto;
import org.example.backend.mappers.OAuth2AuthorizationMapper;
import org.example.backend.mappers.mapstruct.AuthorizationMapper;
import org.example.backend.models.entities.Authorization;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.repositories.AuthorizationRepository;
import org.example.backend.utils.SecurityUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationService implements OAuth2AuthorizationService {

    private final AuthorizationMapper authorizationMapper;
    private final AuthorizationRepository authorizationRepository;
    private final OAuth2AuthorizationMapper oAuth2AuthorizationMapper;

    public List<AuthorizationDto> getAllUserAuthorizations() {
        return authorizationRepository.findAllDtosByUserId(SecurityUtils.getCurrentUserId());
    }

    public List<AuthorizationDto> getAllUserAuthorizationsByDeviceId(String deviceId) {
        return authorizationRepository.findAllDtosByUserIdAndDeviceId(SecurityUtils.getCurrentUserId(), deviceId);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        return authorizationRepository.findById(id)
                .map(oAuth2AuthorizationMapper::toObject)
                .orElse(null);
    }

    @Override
    @Transactional
    public void save(OAuth2Authorization oAuth2Authorization) {
        Authorization authorization = oAuth2AuthorizationMapper.toEntity(oAuth2Authorization);
        Optional<Authorization> existing = authorizationRepository.findById(oAuth2Authorization.getId());

        if (existing.isPresent()) {
            authorizationMapper.mergeAuthorizations(authorization, existing.get());
            authorizationRepository.save(existing.get());
        } else {
            authorizationRepository.save(authorization);
        }
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

    @Override
    public void remove(OAuth2Authorization authorization) {
        authorizationRepository.deleteById(authorization.getId());
    }

    @Transactional
    public void deleteAuthorizationByUserAndId(String id) {
        authorizationRepository.deleteByUserIdAndId(SecurityUtils.getCurrentUserId(), id);
    }

    public void deleteAuthorizationsByUserAndDevice(User user, UserDevice userDevice) {
        authorizationRepository.deleteAllByUserIdAndDeviceId(user.getId(), userDevice.getId());
    }

    public void deleteAllByUserId(String userId) {
        authorizationRepository.deleteAllByUserId(userId);
    }
}
