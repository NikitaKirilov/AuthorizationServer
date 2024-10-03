create table user_role
(
    id   serial primary key,
    name varchar(255) not null unique
);

insert into user_role(name)
VALUES ('USER');

create table app_user
(
    id             serial primary key,
    role_id        int       default 1,

    email          varchar(255) not null unique,
    email_verified boolean   default false,

    password       varchar(255) not null,

    name           varchar(255) not null,
    family_name    varchar(255) not null,

    created_at     timestamp default now(),
    updated_at     timestamp default now()
);

insert into app_user(email, password, name, family_name)
VALUES ('test@test.com',
        '1234',
        'test',
        'test');
