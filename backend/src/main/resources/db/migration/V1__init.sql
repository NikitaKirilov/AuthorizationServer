CREATE SCHEMA IF NOT EXISTS authorization_server;

SET search_path TO authorization_server;

CREATE TABLE app_user
(
    id                     VARCHAR(255) NOT NULL,
    client_registration_id VARCHAR(100),
    email                  VARCHAR(255) NOT NULL,
    email_verified         BOOLEAN,
    password               VARCHAR(255),
    name                   VARCHAR(255),
    given_name             VARCHAR(255),
    family_name            VARCHAR(255),
    last_login             TIMESTAMP WITHOUT TIME ZONE,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT app_user_pkey PRIMARY KEY (id)
);

CREATE TABLE authority
(
    id                   VARCHAR(100) NOT NULL,
    registered_client_id VARCHAR(100),
    name                 VARCHAR(100) NOT NULL,
    description          VARCHAR(1000),
    created_at           TIMESTAMP WITHOUT TIME ZONE,
    updated_at           TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT authority_pkey PRIMARY KEY (id)
);

CREATE TABLE email_verification_token
(
    id         VARCHAR(100)                NOT NULL,
    user_id    VARCHAR(100)                NOT NULL,
    active     BOOLEAN,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT email_verification_token_pkey PRIMARY KEY (id)
);

CREATE TABLE oauth2_registered_client
(
    id                            VARCHAR(100)                              NOT NULL,
    client_id                     VARCHAR(100)                              NOT NULL,
    client_id_issued_at           TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    client_secret                 VARCHAR(200),
    client_secret_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    client_name                   VARCHAR(200)                              NOT NULL,
    client_authentication_methods VARCHAR(1000)                             NOT NULL,
    authorization_grant_types     VARCHAR(1000)                             NOT NULL,
    redirect_uris                 VARCHAR(1000),
    post_logout_redirect_uris     VARCHAR(1000),
    scopes                        VARCHAR(1000)                             NOT NULL,
    client_settings               VARCHAR(2000)                             NOT NULL,
    token_settings                VARCHAR(2000)                             NOT NULL,
    CONSTRAINT oauth2_registered_client_pkey PRIMARY KEY (id)
);

CREATE TABLE user_authority
(
    user_id      VARCHAR(100) NOT NULL,
    authority_id VARCHAR(100) NOT NULL,
    CONSTRAINT user_authority_pkey PRIMARY KEY (user_id, authority_id)
);

CREATE UNIQUE INDEX app_user_email_verified ON app_user (email) WHERE (email_verified) = TRUE;

ALTER TABLE authority
    ADD CONSTRAINT authority_registered_client_id_fkey FOREIGN KEY (registered_client_id) REFERENCES oauth2_registered_client (id) ON DELETE NO ACTION;

ALTER TABLE email_verification_token
    ADD CONSTRAINT email_verification_token_user_id_fkey FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE;

ALTER TABLE user_authority
    ADD CONSTRAINT user_authority_authority_id_fkey FOREIGN KEY (authority_id) REFERENCES authority (id) ON DELETE CASCADE;

ALTER TABLE user_authority
    ADD CONSTRAINT user_authority_user_id_fkey FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE;
