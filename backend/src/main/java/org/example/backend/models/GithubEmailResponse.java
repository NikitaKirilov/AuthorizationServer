package org.example.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubEmailResponse {

    private String email;
    private boolean primary;
    private boolean verified;
    private String visibility;
}
