SET search_path TO authorization_server;

INSERT INTO authority
VALUES (gen_random_uuid()::varchar, null, 'USER', 'Default user authority', now(), null);
