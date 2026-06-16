package org.example.backend.repositories;

import org.example.backend.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r from Role r WHERE r.resource = 'AS' AND r.name = 'USER'")
    Role getASUserRole();
}
