create table scope
(
    id            serial primary key,
    resource_name varchar(255) not null,
    name          varchar(255) not null
);


insert into scope(resource_name, name)
VALUES ('AUTHORIZATION_SERVER', 'USER');



create table app_user
(
    id                  varchar(255),
    idp_registration_id varchar(100),

    email               varchar(255) unique not null,
    email_verified      boolean,

    password            varchar(255),

    name                varchar(255),
    given_name          varchar(255),
    family_name         varchar(255),

    last_login          timestamp,

    created_at          timestamp default now(),
    updated_at          timestamp default now(),

    primary key (id),
    foreign key (idp_registration_id) references idp_registration (id)
);



insert into app_user(id, email, password, name, given_name, family_name)
VALUES ('sdgfsdg-gdsg4232-dsfafsa',
        'test@test.com',
        '$2a$12$o.8JB9HcR5Oe4M1JtlE/Gub.qMstq68uyAaIY26MjOlJ3c5KzHBV.', -- password is 1234
        'test',
        'test');



create table app_user_scope
(
    user_id  varchar(255) not null,
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



create table idp_registration
(
    id              varchar(100)        not null,
    registration_id varchar(100) unique not null,
    client_id       varchar(100)        not null,
    client_secret   varchar(200)        not null,
    client_name     varchar(100)        not null,
    name            varchar(200),
    description     varchar(1000),
    type            varchar(100),
    image           bytea,
    created_at      timestamp           not null,
    updated_at      timestamp           not null,
    primary key (id)
);

-- TODO: replace with liquibase migration
