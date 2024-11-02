package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.HttpMethod.GET;

@UtilityClass
public class RequestUtils {

    public static RequestEntity<?> createOAuth2EmailRequest(OAuth2UserRequest userRequest, String requestUri) {
        HttpHeaders headers = new HttpHeaders();
        URI uri = UriComponentsBuilder
                .fromUriString(requestUri)
                .build()
                .toUri();

        headers.setBearerAuth(userRequest.getAccessToken().getTokenValue());

        return new RequestEntity<>(headers, GET, uri);
    }
}
