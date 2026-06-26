package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RoleDto;
import org.example.backend.exceptions.RoleNotFoundException;
import org.example.backend.mappers.mapstruct.RoleMapper;
import org.example.backend.models.entities.Role;
import org.example.backend.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public Role getASUserRole() {
        return roleRepository.getASUserRole();
    }

    public Role getASAdminRole() {
        return roleRepository.getASAdminRole();
    }

    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found by id: " + id));
    }

    public List<Role> getRolesByIds(Set<String> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::mapToDto)
                .toList();
    }
}
