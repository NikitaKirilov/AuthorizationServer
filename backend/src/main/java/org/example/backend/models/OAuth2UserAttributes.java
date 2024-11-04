package org.example.backend.models;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class OAuth2UserAttributes {

    public static final String LOGIN = "login";

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
}
