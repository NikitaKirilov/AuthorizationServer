package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getOptionalByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public Optional<User> getOptionalById(String id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User saveUserOnIdpLogin(User user) {
        String email = user.getEmail();
        IdpRegistration idpRegistration = user.getIdpRegistration();
        Timestamp lastLogin = user.getLastLogin();

        user = getOptionalByEmail(email)
                .map(existingUser -> {
                    existingUser.setIdpRegistration(idpRegistration);
                    existingUser.setLastLogin(lastLogin);
                    return existingUser;
                }).orElse(user);

        return save(user);
    }
}
