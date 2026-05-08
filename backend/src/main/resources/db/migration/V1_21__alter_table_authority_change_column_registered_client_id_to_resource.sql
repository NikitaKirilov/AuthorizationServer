ALTER TABLE authority
    DROP COLUMN registered_client_id;

ALTER TABLE authority
    ADD COLUMN resource VARCHAR(100) DEFAULT 'AS' NOT NULL;

ALTER TABLE authority
    ADD CONSTRAINT authority_resource_name_key UNIQUE (resource, name)
