package org.example.backend.configs.security;

import org.example.backend.exceptions.AuthException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.oauth2.core.AuthenticationMethod.FORM;
import static org.springframework.security.oauth2.core.AuthenticationMethod.HEADER;
import static org.springframework.security.oauth2.core.AuthenticationMethod.QUERY;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;

@Component
public class OAuth2UserInfoRequestConverter implements Converter<OAuth2UserRequest, RequestEntity<?>> {

    private static final MediaType DEFAULT_CONTENT_TYPE = MediaType
            .valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

    @Override
    public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        ProviderDetails providerDetails = userRequest.getClientRegistration().getProviderDetails();
        AuthenticationMethod authMethod = providerDetails.getUserInfoEndpoint().getAuthenticationMethod();
        URI userInfoUri = UriComponentsBuilder.fromUriString(providerDetails.getUserInfoEndpoint().getUri()).build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (FORM.equals(authMethod)) {
            headers.setContentType(DEFAULT_CONTENT_TYPE);
            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
            formParameters.add(ACCESS_TOKEN, accessToken);

            return new RequestEntity<>(formParameters, headers, POST, userInfoUri);
        } else if (QUERY.equals(authMethod)) {
            URI uriWithToken = UriComponentsBuilder.fromUri(userInfoUri)
                    .queryParam(ACCESS_TOKEN, accessToken)
                    .build().toUri();

            return new RequestEntity<>(headers, GET, uriWithToken);
        } else if (HEADER.equals(authMethod)) {
            headers.setBearerAuth(accessToken);
            return new RequestEntity<>(headers, GET, userInfoUri);
        }

        throw new AuthException("Unsupported idp registration authentication method");
    }
}
