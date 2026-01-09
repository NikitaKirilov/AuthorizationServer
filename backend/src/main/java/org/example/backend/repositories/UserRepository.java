package org.example.backend.repositories;

import org.example.backend.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByEmail(String email);

    Optional<User> findByEmailAndEmailVerifiedTrue(String email);

    void deleteAllByEmailAndEmailVerifiedFalse(String email);

}
