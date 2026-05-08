DELETE
FROM authorization_server.authority
WHERE name = 'USER'
  AND resource = 'AS';


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'READ_OWN_PROFILE',
        'Authorization server read own profile authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'UPDATE_OWN_PROFILE',
        'Authorization server update own profile authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'UPDATE_OWN_PASSWORD',
        'Authorization server update own password authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'DELETE_OWN_ACCOUNT',
        'Authorization server delete own profile authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'ADD_OAUTH2_CLIENT',
        'Authorization server add oauth2 client authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'UPDATE_OAUTH2_CLIENT',
        'Authorization server update oauth2 client authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'DELETE_OAUTH2_CLIENT',
        'Authorization server delete oauth2 client authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'ASSIGN_ROLE',
        'Authorization server assign role authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'READ_USERS_PROFILES',
        'Authorization server read users profiles authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'UPDATE_USERS_PROFILES',
        'Authorization server update users profiles authority',
        NOW(),
        NOW());


INSERT INTO authority (id, name, description, created_at, updated_at)
VALUES (gen_random_uuid()::VARCHAR,
        'DELETE_USERS_PROFILES',
        'Authorization server delete users profiles authority',
        NOW(),
        NOW());
