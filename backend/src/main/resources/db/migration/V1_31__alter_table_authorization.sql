ALTER TABLE "authorization"
    RENAME COLUMN registered_client_id TO oauth2_client_id;

ALTER TABLE "authorization"
    ADD COLUMN device_id VARCHAR(100) REFERENCES user_device (id) ON DELETE CASCADE;

ALTER TABLE "authorization"
    ADD COLUMN user_id VARCHAR(100) REFERENCES app_user (id) ON DELETE CASCADE;

ALTER TABLE "authorization"
    ADD COLUMN created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE "authorization"
    ADD COLUMN updated_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE "authorization"
    ADD CONSTRAINT authorization_oauth2_client_id_fkey FOREIGN KEY (oauth2_client_id)
        REFERENCES oauth2_client (id) ON DELETE CASCADE;
