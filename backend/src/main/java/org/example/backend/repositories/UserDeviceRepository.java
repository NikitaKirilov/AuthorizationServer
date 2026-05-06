package org.example.backend.repositories;

import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, String> {

    Optional<UserDevice> findByUserAndDetailsAndLocation(User user, String deviceDetails, String location);
}
