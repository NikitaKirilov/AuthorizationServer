package org.example.backend.repositories;

import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRegistrationWrapperRepository extends JpaRepository<ClientRegistrationWrapper, String> {

    Optional<ClientRegistrationWrapper> findByRegistrationId(String registrationId);

}
