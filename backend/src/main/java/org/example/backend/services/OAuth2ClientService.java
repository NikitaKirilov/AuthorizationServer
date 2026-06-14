package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.backend.dtos.OAuth2ClientDto;
import org.example.backend.exceptions.OAuth2ClientNotFoundException;
import org.example.backend.mappers.RegisteredClientMapper;
import org.example.backend.mappers.mapstruct.OAuth2ClientMapper;
import org.example.backend.models.entities.OAuth2Client;
import org.example.backend.repositories.OAuth2ClientRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2ClientService implements RegisteredClientRepository {

    private static final ExampleMatcher EXAMPLE_MATCHER = ExampleMatcher.matchingAny()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnorePaths(
                    "scopes", "redirectUris", "postLogoutRedirectUris",
                    "jwtSetUrl", "accessTokenTimeToLive", "refreshTokenTimeToLive",
                    "requireProofKey", "requireAuthorizationConsent", "reuseRefreshTokens",
                    "createdAt", "updatedAt"
            );

    private static final int CLIENT_SECRET_LENGTH = 30;

    private final RegisteredClientMapper registeredClientMapper;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2ClientMapper oAuth2ClientMapper;
    private final OAuth2ClientRepository oAuth2ClientRepository;

    public Page<OAuth2ClientDto> getAllOAuth2Clients(OAuth2ClientDto oAuth2ClientDto, Pageable pageable) {
        OAuth2Client probe = oAuth2ClientMapper.mapDtoToEntity(oAuth2ClientDto);
        return oAuth2ClientRepository.findAll(Example.of(probe, EXAMPLE_MATCHER), pageable)
                .map(oAuth2ClientMapper::mapEntityToDto);
    }

    public OAuth2ClientDto getOAuth2ClientByClientId(String clientId) {
        return oAuth2ClientRepository.findByClientId(clientId)
                .map(oAuth2ClientMapper::mapEntityToDto)
                .orElseThrow(() -> new OAuth2ClientNotFoundException(clientId));
    }

    @Override
    public @Nullable RegisteredClient findById(String id) {
        return oAuth2ClientRepository.findById(id)
                .map(registeredClientMapper::mapToObject)
                .orElse(null);
    }

    @Override
    public @Nullable RegisteredClient findByClientId(String clientId) {
        return oAuth2ClientRepository.findByClientId(clientId)
                .map(registeredClientMapper::mapToObject)
                .orElse(null);
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        oAuth2ClientRepository.save(registeredClientMapper.mapToEntity(registeredClient));
    }

    @Transactional
    public OAuth2ClientDto createOrUpdate(OAuth2ClientDto oAuth2ClientDto) {
        OAuth2Client client = oAuth2ClientRepository.findByClientId(oAuth2ClientDto.getClientId())
                .orElseGet(OAuth2Client::new);

        if (client.getId() == null) {
            client.setId(UUID.randomUUID().toString());
        }

        oAuth2ClientMapper.mergeDto(oAuth2ClientDto, client);

        return oAuth2ClientMapper.mapEntityToDto(oAuth2ClientRepository.save(client));
    }

    @Transactional
    public String generateClientSecret(String clientId) {
        OAuth2Client client = oAuth2ClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new OAuth2ClientNotFoundException("Client with id " + clientId + " not found"));

        String secret = RandomStringUtils.secure().nextAlphanumeric(CLIENT_SECRET_LENGTH);
        String encodedSecret = passwordEncoder.encode(secret);

        client.setClientSecret(encodedSecret);

        oAuth2ClientRepository.save(client);

        return secret;
    }

    /**
     * Deletes OAuth2 client. <br>
     * Associated authorizations are removed by database ON DELETE CASCADE constraints.
     */
    @Transactional
    public void deleteOAuth2Client(String clientId) {
        oAuth2ClientRepository.deleteByClientId(clientId);
    }
}
