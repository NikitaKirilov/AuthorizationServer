package org.example.backend.configs.client;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_JWT;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

@Component
public class ClientRegistrationRowMapper implements RowMapper<ClientRegistration> {

    private final List<ClientAuthenticationMethod> supportedAuthenticationMethods = List.of(CLIENT_SECRET_BASIC, CLIENT_SECRET_POST, CLIENT_SECRET_JWT);

    @Override
    public ClientRegistration mapRow(ResultSet rs, int rowNum) throws SQLException {
        String[] scopes = StringUtils.commaDelimitedListToStringArray(rs.getString("scopes"));
        ClientAuthenticationMethod clientAuthenticationMethod =
                getClientAuthenticationMethod(rs.getString("client_authentication_method"));
        AuthorizationGrantType authorizationGrantType =
                getAuthorizationGrantType(rs.getString("authorization_grant_type"));

        return ClientRegistration.withRegistrationId(rs.getString("registration_id"))
                .clientId(rs.getString("client_id"))
                .clientSecret(rs.getString("client_secret"))
                .clientAuthenticationMethod(clientAuthenticationMethod)
                .authorizationGrantType(authorizationGrantType)
                .redirectUri(rs.getString("redirect_uri"))
                .scope(scopes)
                .authorizationUri(rs.getString("authorization_uri"))
                .tokenUri(rs.getString("token_uri"))
                .userInfoUri(rs.getString("user_info_uri"))
                .jwkSetUri(rs.getString("jwk_set_uri"))
                .build();
    }

    public ClientAuthenticationMethod getClientAuthenticationMethod(String method) {
        return supportedAuthenticationMethods.stream()
                .filter(authenticationMethod -> authenticationMethod.getValue().equals(method))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported method: " + method) //TODO: replace with own exception
                );
    }

    public AuthorizationGrantType getAuthorizationGrantType(String grantType) {
        if (AUTHORIZATION_CODE.getValue().equals(grantType)) {
            return AUTHORIZATION_CODE;
        }
        throw new RuntimeException("Unsupported grant type: " + grantType); //TODO: replace with own exception
    }
}
