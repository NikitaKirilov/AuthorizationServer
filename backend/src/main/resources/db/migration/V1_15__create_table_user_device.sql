CREATE TABLE user_device
(
    id             VARCHAR(100) NOT NULL,
    user_id        VARCHAR(100) NOT NULL,
    details        VARCHAR(255) NOT NULL,
    location       VARCHAR(255) NOT NULL,
    last_logged_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT user_device_pkey PRIMARY KEY (id),
    CONSTRAINT user_device_user_id_fkey FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE,
    CONSTRAINT user_device_user_id_details_location_key UNIQUE (user_id, details, location)
)
