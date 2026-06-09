package org.example.backend.repositories;

import org.example.backend.models.entities.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, String> {

    Optional<OAuth2Client> findByClientId(String clientId);

    void deleteByClientId(String clientId);
}
