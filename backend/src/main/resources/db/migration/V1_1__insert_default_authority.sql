SET search_path TO authorization_server;

INSERT INTO authority
VALUES (gen_random_uuid()::VARCHAR, NULL, 'USER', 'Default user authority', NOW(), NULL);
