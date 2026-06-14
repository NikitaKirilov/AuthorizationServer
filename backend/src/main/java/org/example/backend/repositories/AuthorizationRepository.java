package org.example.backend.repositories;

import org.example.backend.dtos.AuthorizationDto;
import org.example.backend.models.entities.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, String> {

    Optional<Authorization> findByState(String state);

    Optional<Authorization> findByAuthorizationCodeValue(String authorizationCode);

    Optional<Authorization> findByAccessTokenValue(String accessToken);

    Optional<Authorization> findByRefreshTokenValue(String refreshToken);

    Optional<Authorization> findByOidcIdTokenValue(String idToken);

    Optional<Authorization> findByUserCodeValue(String userCode);

    Optional<Authorization> findByDeviceCodeValue(String deviceCode);

    @Query("select a from Authorization a where a.state = :token" +
            " or a.authorizationCodeValue = :token" +
            " or a.accessTokenValue = :token" +
            " or a.refreshTokenValue = :token" +
            " or a.oidcIdTokenValue = :token" +
            " or a.userCodeValue = :token" +
            " or a.deviceCodeValue = :token"
    )
    Optional<Authorization> findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
            @Param("token") String token
    );

    List<Authorization> findAllByUserId(String userId);

    List<Authorization> findAllByUserIdAndOauth2ClientId(String userId, String oAuth2ClientId);

    void deleteByUserIdAndId(String userId, String id);

    void deleteAllByUserId(String userId);

    void deleteAllByUserIdAndDeviceId(String userId, String deviceId);

    @Query("""
            SELECT new org.example.backend.dtos.AuthorizationDto(
                    a.id,
                    c.clientId,
                    a.authorizedScopes,
                    c.clientName,
                    a.createdAt,
                    a.updatedAt
                    )
                FROM Authorization a JOIN OAuth2Client c ON a.oauth2ClientId = c.id
                        WHERE a.userId = :userId AND a.deviceId = :deviceId
            """
    )
    List<AuthorizationDto> findAllDtosByUserIdAndDeviceId(String userId, String deviceId);

    @Query("""
            SELECT new org.example.backend.dtos.AuthorizationDto(
                    a.id,
                    c.clientId,
                    a.authorizedScopes,
                    c.clientName,
                    a.createdAt,
                    a.updatedAt
                    )
                FROM Authorization a JOIN OAuth2Client c ON a.oauth2ClientId = c.id
                        WHERE a.userId = :userId
            """
    )
    List<AuthorizationDto> findAllDtosByUserId(String userId);
}
