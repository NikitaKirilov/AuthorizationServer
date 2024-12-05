package org.example.backend.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.models.enums.OAuth2ProviderType;

import java.sql.Timestamp;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegistrationWrapper {

    @Id
    private String id;

    @OneToMany(mappedBy = "clientRegistrationWrapper")
    private List<User> users;

    private String registrationId;

    private String clientId;
    private String clientSecret;
    private String clientName;

    private String name;
    private String description;

    @Enumerated(value = STRING)
    private OAuth2ProviderType type;

    private byte[] image;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
