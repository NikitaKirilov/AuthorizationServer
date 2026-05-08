INSERT INTO role_authority (role_id, authority_id)
SELECT r.id, a.id
FROM role r
         CROSS JOIN authority a
WHERE r.name = 'USER'
  AND r.resource = 'AS'
  AND a.name IN (
                 'READ_OWN_PROFILE',
                 'UPDATE_OWN_PROFILE',
                 'UPDATE_OWN_PASSWORD',
                 'DELETE_OWN_ACCOUNT'
    );



INSERT INTO role_authority (role_id, authority_id)
SELECT r.id, a.id
FROM role r
         CROSS JOIN authority a
WHERE r.name = 'ADMIN'
  AND r.resource = 'AS'
  AND a.name IN (
                 'READ_OWN_PROFILE',
                 'UPDATE_OWN_PROFILE',
                 'UPDATE_OWN_PASSWORD',
                 'DELETE_OWN_ACCOUNT',
                 'ADD_OAUTH2_CLIENT',
                 'UPDATE_OAUTH2_CLIENT',
                 'DELETE_OAUTH2_CLIENT',
                 'ASSIGN_ROLE',
                 'READ_USERS_PROFILES',
                 'UPDATE_USERS_PROFILES',
                 'DELETE_USERS_PROFILES'
    );
