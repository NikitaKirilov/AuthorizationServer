package org.example.backend.utils;

import lombok.experimental.UtilityClass;

import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

@UtilityClass
public class StringUtils {

    private static final String PATH_DELIMITER = "/";

    public static String getAuthorizationRequestUri(String registrationId) {
        return DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + PATH_DELIMITER + registrationId;
    }
}
