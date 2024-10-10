package org.example.backend.configs.client;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.ClientRegistrationDto;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository {

    private static final String CLIENT_REGISTRATION_COLUMNS =
            "registration_id, " +
            "client_id, " +
            "client_secret, " +
            "client_authentication_method, " +
            "authorization_grant_type, " +
            "redirect_uri, " +
            "scopes, " +
            "authorization_uri, " +
            "token_uri, " +
            "user_info_endpoint, " +
            "jwt_set_uri";

    private static final String TABLE_NAME = "oauth2_client_registration";

    private static final String GET_BY_REGISTRATION_ID = "SELECT * FROM " + TABLE_NAME + " WHERE registration_id = ?";

    private static final String SAVE_CLIENT_REGISTRATION = "INSERT INTO " + TABLE_NAME + "(" + CLIENT_REGISTRATION_COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final JdbcOperations jdbcOperations;
    private final ClientRegistrationParametersMapper parametersMapper;
    private final ClientRegistrationRowMapper rowMapper;

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        List<ClientRegistration> clientRegistrations =
                jdbcOperations.query(GET_BY_REGISTRATION_ID, rowMapper, registrationId);

        return clientRegistrations.isEmpty() ? null : clientRegistrations.getFirst();
    }

    public void save(ClientRegistrationDto clientRegistrationDto) {
        ClientRegistration existingClientRegistration = findByRegistrationId(clientRegistrationDto.getRegistrationId());

        //TODO: check ClienRegistration with same client_id, client_secret not exists
        if (existingClientRegistration != null) {
            throw new RuntimeException(format("Client registration with registration id '%s' already exists",
                    existingClientRegistration.getRegistrationId())); //TODO: replace with own exception
        }

        ClientRegistration clientRegistration = clientRegistrationDto.mapToClientRegistration();
        List<SqlParameterValue> parameters = parametersMapper.apply(clientRegistration);
        PreparedStatementSetter preparedStatementSetter = new ArgumentPreparedStatementSetter(parameters.toArray());

        jdbcOperations.update(SAVE_CLIENT_REGISTRATION, preparedStatementSetter);
    }

    //TODO: updateClientRegistration
}
