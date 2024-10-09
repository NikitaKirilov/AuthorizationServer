create table scope
(
    id   serial primary key,
    name varchar(255) not null unique
);

insert into scope(name)
VALUES ('USER');



create table app_user
(
    id             serial primary key,

    email          varchar(255) not null unique,
    email_verified boolean   default false,

    password       varchar(255),

    name           varchar(255) not null,
    family_name    varchar(255) not null,

    created_at     timestamp default now(),
    updated_at     timestamp default now()
);

insert into app_user(email, password, name, family_name)
VALUES ('test@test.com',
        '$2a$12$o.8JB9HcR5Oe4M1JtlE/Gub.qMstq68uyAaIY26MjOlJ3c5KzHBV.', -- password is 1234
        'test',
        'test');



create table app_user_scope
(
    user_id  int not null,
    scope_id int default 1,
    primary key (user_id, scope_id)
);

insert into app_user_scope(user_id, scope_id)
VALUES (1, 1);



create table oauth2_registered_client
(
    id                            varchar(100)                            NOT NULL,
    client_id                     varchar(100)                            NOT NULL,
    client_id_issued_at           timestamp     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(200)  DEFAULT NULL,
    client_secret_expires_at      timestamp     DEFAULT NULL,
    client_name                   varchar(200)                            NOT NULL,
    client_authentication_methods varchar(1000)                           NOT NULL,
    authorization_grant_types     varchar(1000)                           NOT NULL,
    redirect_uris                 varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris     varchar(1000) DEFAULT NULL,
    scopes                        varchar(1000)                           NOT NULL,
    client_settings               varchar(2000)                           NOT NULL,
    token_settings                varchar(2000)                           NOT NULL,
    primary key (id)
);
