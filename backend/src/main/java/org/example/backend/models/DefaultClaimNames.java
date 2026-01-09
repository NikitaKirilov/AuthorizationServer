package org.example.backend.models;

import lombok.Getter;

@Getter
public enum DefaultClaimNames {

    EMAIL("email"),
    EMAIL_VERIFIED("email_verified"),
    NAME("name"),
    GIVEN_NAME("given_name"),
    FAMILY_NAME("family_name");

    private final String value;

    DefaultClaimNames(String value) {
        this.value = value;
    }

}
