SET search_path TO authorization_server;

ALTER TABLE email_verification_token
    ADD COLUMN code_hash VARCHAR(255);
