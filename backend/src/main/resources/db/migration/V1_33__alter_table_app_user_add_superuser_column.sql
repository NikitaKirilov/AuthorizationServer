ALTER TABLE app_user
    ADD COLUMN superuser BOOLEAN DEFAULT NULL;

CREATE UNIQUE INDEX only_one_superuser ON app_user (superuser) WHERE superuser
