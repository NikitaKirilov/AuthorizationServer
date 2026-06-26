package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.RoleDto;
import org.example.backend.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }
}
