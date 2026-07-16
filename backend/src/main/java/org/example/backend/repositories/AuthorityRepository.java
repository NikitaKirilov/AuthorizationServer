package org.example.backend.repositories;

import org.example.backend.models.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    boolean existsByResourceAndNameAndIdNot(String resource, String name, String id);
}
