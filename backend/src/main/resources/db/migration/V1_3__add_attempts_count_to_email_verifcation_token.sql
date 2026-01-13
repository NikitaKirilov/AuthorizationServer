ALTER TABLE email_verification_token
    ADD COLUMN attempts_count INT NOT NULL DEFAULT 0;
