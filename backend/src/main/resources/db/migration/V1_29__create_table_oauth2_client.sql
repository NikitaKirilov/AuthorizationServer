CREATE TABLE oauth2_client
(
    id                            VARCHAR(100) NOT NULL,
    client_id                     VARCHAR(20)  NOT NULL,
    client_secret                 VARCHAR(100),
    client_name                   VARCHAR(255) NOT NULL,
    client_description            TEXT,
    scopes                        TEXT         NOT NULL,
    redirect_uris                 VARCHAR(1000),
    post_logout_redirect_uris     VARCHAR(1000),
    jwk_set_url                   VARCHAR(1000),
    require_proof_key             BOOLEAN DEFAULT FALSE,
    require_authorization_consent BOOLEAN DEFAULT FALSE,
    reuse_refresh_tokens           BOOLEAN DEFAULT FALSE,
    access_token_time_to_live     NUMERIC      NOT NULL,
    refresh_token_time_to_live    NUMERIC      NOT NULL,

    CONSTRAINT oauth2_client_pk PRIMARY KEY (id),
    CONSTRAINT oauth2_client_client_id_key UNIQUE (client_id)
);

INSERT INTO oauth2_client (id,
                           client_id,
                           client_secret,
                           client_name,
                           client_description,
                           scopes,
                           redirect_uris,
                           post_logout_redirect_uris,
                           jwk_set_url,
                           require_proof_key,
                           require_authorization_consent,
                           reuse_refresh_tokens,
                           access_token_time_to_live,
                           refresh_token_time_to_live)
VALUES (gen_random_uuid()::VARCHAR,
        'client_id',
        '$2a$10$oreY9EN9Cpi28WRMBRprzeGFnex.uPe5qoMoFvay8F9UrXrgNh0Q2', -- initial value: nylSCVxGOzz7uwjS4APtSO1gzl6reP
        'Test client',
        'Test OAuth2 client not for production',
        'openid,profile,email',
        'http://localhost:3030/app/code',
        NULL,
        NULL,
        FALSE,
        TRUE,
        FALSE,
        900000000000,
        2592000000000000);
