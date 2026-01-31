CREATE UNIQUE INDEX email_verification_token_active ON email_verification_token (user_id) WHERE (active) = TRUE;
