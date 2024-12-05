package org.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.models.enums.OAuth2ProviderType;

@Data
public class ClientRegistrationWrapperDto {

    @JsonProperty("registration_id")
    private String registrationId;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("client_name")
    private String clientName;

    private String name;
    private String description;

    private OAuth2ProviderType type;

    @JsonIgnore
    public ClientRegistrationWrapper toEntity() {
        return new ClientRegistrationWrapper()
                .setRegistrationId(this.registrationId)

                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setClientName(this.clientName)

                .setName(this.name)
                .setDescription(this.description)

                .setType(this.type);
    }
}
