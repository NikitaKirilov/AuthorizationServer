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
    id                            varchar(100)                            not null,
    client_id                     varchar(100)                            not null,
    client_id_issued_at           timestamp     default current_timestamp not null,
    client_secret                 varchar(200)  default null,
    client_secret_expires_at      timestamp     default null,
    client_name                   varchar(200)                            not null,
    client_authentication_methods varchar(1000)                           not null,
    authorization_grant_types     varchar(1000)                           not null,
    redirect_uris                 varchar(1000) default null,
    post_logout_redirect_uris     varchar(1000) default null,
    scopes                        varchar(1000)                           not null,
    client_settings               varchar(2000)                           not null,
    token_settings                varchar(2000)                           not null,
    primary key (id)
);



create table oauth2_client_registration
(
    registration_id              varchar(100)        not null,
    client_id                    varchar(100) unique not null,
    client_secret                varchar(200) unique not null,
    client_authentication_method varchar(100)        not null,
    authorization_grant_type     varchar(100)        not null,
    redirect_uri                 varchar(500)        not null,
    scopes                       varchar(1000)       not null,
    authorization_uri            varchar(200)        not null,
    token_uri                    varchar(500)        not null,
    user_info_uri                varchar(500)        not null,
    jwk_set_uri                  varchar(500)        not null,
    primary key (registration_id)
);

-- TODO: split tables to different files
