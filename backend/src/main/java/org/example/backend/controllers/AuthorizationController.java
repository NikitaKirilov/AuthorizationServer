package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.AuthorizationDto;
import org.example.backend.services.AuthorizationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/authorizations")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping
    public List<AuthorizationDto> getAllUserAuthorizations() {
        return authorizationService.getAllUserAuthorizations();
    }

    @GetMapping("/{deviceId}")
    public List<AuthorizationDto> getAllUserAuthorizationsByDeviceId(@PathVariable String deviceId) {
        return authorizationService.getAllUserAuthorizationsByDeviceId(deviceId);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthorizationByUserAndId(@PathVariable String id) {
        authorizationService.deleteAuthorizationByUserAndId(id);
    }
}
