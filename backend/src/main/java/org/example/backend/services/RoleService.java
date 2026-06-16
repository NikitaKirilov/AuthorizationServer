package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.exceptions.RoleNotFoundException;
import org.example.backend.models.entities.Role;
import org.example.backend.repositories.RoleRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getASUserRole() {
        return roleRepository.getASUserRole();
    }

    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found by id: " + id));
    }
}
