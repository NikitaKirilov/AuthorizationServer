package org.example.backend.models.enums;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import static org.example.backend.models.TokenClaimNames.ID;
import static org.springframework.security.oauth2.core.AuthenticationMethod.QUERY;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.NONE;
import static org.springframework.security.oauth2.core.oidc.IdTokenClaimNames.SUB;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;
import static org.springframework.security.oauth2.core.oidc.OidcScopes.PROFILE;

public enum OAuth2ProviderType {

    GOOGLE {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder(registrationId, CLIENT_SECRET_BASIC)
                    .scope(PROFILE, EMAIL)
                    .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                    .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                    .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                    .issuerUri("https://accounts.google.com")
                    .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .userNameAttributeName(SUB);
        }
    },

    GITHUB {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder(registrationId, CLIENT_SECRET_BASIC)
                    .scope("read:user", "user:email")
                    .authorizationUri("https://github.com/login/oauth/authorize")
                    .tokenUri("https://github.com/login/oauth/access_token")
                    .userInfoUri("https://api.github.com/user")
                    .userNameAttributeName(ID);
        }
    },

    FACEBOOK {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder(registrationId, CLIENT_SECRET_POST)
                    .scope("public_profile", EMAIL)
                    .authorizationUri("https://www.facebook.com/v2.8/dialog/oauth")
                    .tokenUri("https://graph.facebook.com/v2.8/oauth/access_token")
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email")
                    .userNameAttributeName(ID);
        }
    },

    OKTA {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder(registrationId, NONE)
                    .scope(PROFILE, EMAIL)
                    .userNameAttributeName(SUB);
        }
    },

    YANDEX {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return null;
        }
    },

    MAILRU {
        @Override
        public ClientRegistration.Builder getBuilder(String registrationId) {
            return getBuilder(registrationId, CLIENT_SECRET_BASIC)
                    .scope("userinfo")
                    .authorizationUri("https://oauth.mail.ru/login")
                    .tokenUri("https://oauth.mail.ru/token")
                    .userInfoUri("https://oauth.mail.ru/userinfo")
                    .userInfoAuthenticationMethod(QUERY)
                    .userNameAttributeName(ID);
        }
    };

    private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

    protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientAuthenticationMethod(method)
                .redirectUri(DEFAULT_REDIRECT_URL)
                .authorizationGrantType(AUTHORIZATION_CODE);
    }

    public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
