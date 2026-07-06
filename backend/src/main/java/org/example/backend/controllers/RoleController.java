package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RoleDto;
import org.example.backend.dtos.RoleUpdateDto;
import org.example.backend.models.entities.Role;
import org.example.backend.services.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public Page<RoleDto> getAllRoles(Role probe, Pageable pageable) {
        return roleService.getAllRolesDto(probe, pageable);
    }

    @GetMapping("/{id}")
    public RoleDto getRoleById(@PathVariable String id) {
        return roleService.getRoleDtoById(id);
    }

    @PostMapping
    public RoleDto createRole(@RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.createRole(roleUpdateDto);
    }

    @PutMapping("/{id}")
    public RoleDto updateRole(@PathVariable String id, @RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.updateRole(id, roleUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable String id) {
        roleService.deleteRoleById(id);
    }
}
