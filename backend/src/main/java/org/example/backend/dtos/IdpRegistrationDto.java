package org.example.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.enums.OAuth2ProviderType;

@Data
public class IdpRegistrationDto {

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

    @JsonProperty("image_uri")
    private String imageUri;

    @JsonIgnore
    public IdpRegistration toEntity() {
        return IdpRegistration.builder()
                .registrationId(this.registrationId)
                .clientId(this.clientId)
                .clientSecret(this.clientSecret)
                .clientName(this.clientName)
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .imageUri(this.imageUri)
                .build();
    }
}
