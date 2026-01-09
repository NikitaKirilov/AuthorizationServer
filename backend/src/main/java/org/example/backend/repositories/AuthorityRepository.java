package org.example.backend.repositories;

import org.example.backend.models.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @Query(value = "SELECT a FROM Authority a WHERE a.registeredClientId IS NULL AND a.name = 'USER'")
    Authority getDefaultAuthority();
}
