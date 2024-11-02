package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.IdpRegistrationDto;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.services.IdpRegistrationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/idp-registrations")
@RequiredArgsConstructor
public class IdpRegistrationController {

    private final IdpRegistrationService idpRegistrationService;

    @GetMapping("/{id}")
    public IdpRegistration getIdpRegistration(@PathVariable String id) {
        return idpRegistrationService.getById(id);
    }

    @GetMapping
    public List<IdpRegistration> getAllIdpRegistrations() {
        return idpRegistrationService.getAll();
    }

    @PostMapping
    public IdpRegistration addIdpRegistration(@RequestBody IdpRegistrationDto idpRegistrationDto) { //TODO: add validator
        return idpRegistrationService.saveIdpRegistration(idpRegistrationDto);
    }

    @PatchMapping("/{id}")
    public IdpRegistration updateIdpRegistration(
            @PathVariable String id,
            @RequestBody IdpRegistrationDto idpRegistrationDto
    ) {
        return idpRegistrationService.updateIdpRegistration(id, idpRegistrationDto);
    }

    @DeleteMapping("/{id}")
    public void deleteIdpRegistration(@PathVariable String id) {
        idpRegistrationService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllIdpRegistrations() {
        idpRegistrationService.deleteAll();
    }
}
