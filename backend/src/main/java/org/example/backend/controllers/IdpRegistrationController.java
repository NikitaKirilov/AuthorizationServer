package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.IdpRegistrationDto;
import org.example.backend.models.ClientRegistrationPublicInfo;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.services.IdpRegistrationService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/infos")
    public List<ClientRegistrationPublicInfo> getClientRegistrationPublicInfos() {
        return idpRegistrationService.getClientRegistrationPublicInfos();
    }

    @PostMapping
    public IdpRegistration addIdpRegistration(
            @RequestPart IdpRegistrationDto idpRegistrationDto,
            @RequestPart MultipartFile image
    ) throws IOException { //TODO: add validator
        return idpRegistrationService.saveIdpRegistration(idpRegistrationDto, image);
    }

    @PatchMapping("/{id}")
    public IdpRegistration updateIdpRegistration(
            @PathVariable String id,
            @RequestPart IdpRegistrationDto idpRegistrationDto,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        return idpRegistrationService.updateIdpRegistration(id, idpRegistrationDto, image);
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
