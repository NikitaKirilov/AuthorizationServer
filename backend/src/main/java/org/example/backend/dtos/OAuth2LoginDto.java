package org.example.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2LoginDto {

    private String clientName;
    private String loginUri;
    private String imageUri;
}
