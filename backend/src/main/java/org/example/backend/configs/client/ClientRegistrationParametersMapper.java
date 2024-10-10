package org.example.backend.configs.client;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Function;

import static java.sql.Types.VARCHAR;

@Component
@RequiredArgsConstructor
public class ClientRegistrationParametersMapper implements Function<ClientRegistration, List<SqlParameterValue>> {

    @Override
    public List<SqlParameterValue> apply(ClientRegistration clientRegistration) {
        String clientAuthenticationMethod = clientRegistration.getClientAuthenticationMethod().getValue();
        String authorizationGrantType = clientRegistration.getAuthorizationGrantType().getValue();
        String scopes = StringUtils.collectionToCommaDelimitedString(clientRegistration.getScopes());

        ProviderDetails providerDetails = clientRegistration.getProviderDetails();

        return List.of(
                new SqlParameterValue(VARCHAR, clientRegistration.getRegistrationId()),
                new SqlParameterValue(VARCHAR, clientRegistration.getClientId()),
                new SqlParameterValue(VARCHAR, clientRegistration.getClientSecret()),
                new SqlParameterValue(VARCHAR, clientAuthenticationMethod),
                new SqlParameterValue(VARCHAR, authorizationGrantType),
                new SqlParameterValue(VARCHAR, clientRegistration.getRedirectUri()),
                new SqlParameterValue(VARCHAR, scopes),
                new SqlParameterValue(VARCHAR, providerDetails.getAuthorizationUri()),
                new SqlParameterValue(VARCHAR, providerDetails.getTokenUri()),
                new SqlParameterValue(VARCHAR, providerDetails.getUserInfoEndpoint().getUri()),
                new SqlParameterValue(VARCHAR, providerDetails.getJwkSetUri())
        );
    }
}
