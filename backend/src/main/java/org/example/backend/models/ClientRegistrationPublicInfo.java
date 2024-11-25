package org.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientRegistrationPublicInfo {

    private String registrationId;
    private String clientName;
    private String loginUri;
    private byte[] image;
}
