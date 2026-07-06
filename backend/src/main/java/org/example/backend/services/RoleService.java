package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RoleDto;
import org.example.backend.dtos.RoleUpdateDto;
import org.example.backend.exceptions.RoleAlreadyExists;
import org.example.backend.exceptions.RoleNotFoundException;
import org.example.backend.mappers.mapstruct.RoleMapper;
import org.example.backend.models.entities.Authority;
import org.example.backend.models.entities.Role;
import org.example.backend.repositories.RoleRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    public static final String DEFAULT_RESOURCE = "AS";
    private static final ExampleMatcher EXAMPLE_MATCHER = ExampleMatcher.matchingAll()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withIgnorePaths("createdAt", "updatedAt");

    private final AuthorityService authorityService;
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

    public RoleDto getRoleDtoById(String id) {
        return roleMapper.mapToDto(getRoleById(id));
    }

    public List<Role> getRolesByIds(Set<String> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    public Page<RoleDto> getAllRolesDto(Role probe, Pageable pageable) {
        return roleRepository.findAll(Example.of(probe, EXAMPLE_MATCHER), pageable)
                .map(roleMapper::mapToDto);
    }

    @Transactional
    public RoleDto createRole(RoleUpdateDto dto) {
        checkRoleExists(dto.getResource(), dto.getName(), null);

        Role role = new Role();
        role.setId(UUID.randomUUID().toString());

        mergeDto(role, dto);

        roleRepository.save(role);

        return roleMapper.mapToDto(role);
    }

    @Transactional
    public RoleDto updateRole(String id, RoleUpdateDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found by id: " + id));

        checkRoleExists(dto.getResource(), dto.getName(), role.getId());

        mergeDto(role, dto);

        roleRepository.save(role);

        return roleMapper.mapToDto(role);
    }

    public void deleteRoleById(String id) {
        roleRepository.deleteById(id);
    }

    private void checkRoleExists(String resource, String name, String currentRoleId) {
        if (roleRepository.existsByResourceAndNameAndIdNot(resource, name, currentRoleId)) {
            throw new RoleAlreadyExists("Role with resource '" + resource + "' and name '" + name + "' already exists");
        }
    }

    private void mergeDto(Role role, RoleUpdateDto dto) {
        role.setName(dto.getName());
        role.setResource(Objects.requireNonNullElse(dto.getResource(), DEFAULT_RESOURCE));
        role.setDescription(dto.getDescription());

        Set<Authority> authorities = new HashSet<>(
                authorityService.getAllAuthoritiesByIds(dto.getAuthorityIds())
        );
        role.setAuthorities(authorities);
    }
}
