CREATE TABLE role
(
    id          VARCHAR(100)              NOT NULL,
    name        VARCHAR(100)              NOT NULL,
    resource    VARCHAR(100) DEFAULT 'AS' NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT role_pkey PRIMARY KEY (id),
    CONSTRAINT role_resource_name_key UNIQUE (resource, name)
)
