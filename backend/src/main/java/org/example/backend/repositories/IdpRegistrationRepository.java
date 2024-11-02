package org.example.backend.repositories;

import org.example.backend.models.entities.IdpRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdpRegistrationRepository extends JpaRepository<IdpRegistration, String> {

    Optional<IdpRegistration> findByRegistrationId(String registrationId);

}
