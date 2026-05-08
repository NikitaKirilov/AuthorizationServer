CREATE TABLE role_authority
(
    role_id      VARCHAR(100) NOT NULL,
    authority_id VARCHAR(100) NOT NULL,
    CONSTRAINT role_authority_pkey PRIMARY KEY (role_id, authority_id),
    CONSTRAINT role_authority_role_id_fkey FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    CONSTRAINT role_authority_authority_id_fkey FOREIGN KEY (authority_id) REFERENCES authority (id) ON DELETE CASCADE
)
