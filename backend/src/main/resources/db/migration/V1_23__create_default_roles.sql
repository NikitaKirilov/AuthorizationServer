INSERT INTO role (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'USER',
        'Authorization server default user role',
        NOW(),
        NOW());


INSERT INTO role (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'ADMIN',
        'Authorization server admin role',
        NOW(),
        NOW());
