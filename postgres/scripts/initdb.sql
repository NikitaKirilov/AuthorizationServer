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
