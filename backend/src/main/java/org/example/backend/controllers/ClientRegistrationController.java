package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.ClientRegistrationWrapperDto;
import org.example.backend.models.ClientRegistrationPublicInfo;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.services.ClientRegistrationWrapperService;
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
public class ClientRegistrationController {

    private final ClientRegistrationWrapperService clientRegistrationWrapperService;

    @GetMapping("/{id}")
    public ClientRegistrationWrapper getIdpRegistration(@PathVariable String id) {
        return clientRegistrationWrapperService.getById(id);
    }

    @GetMapping
    public List<ClientRegistrationWrapper> getAllIdpRegistrations() {
        return clientRegistrationWrapperService.getAll();
    }

    @GetMapping("/infos")
    public List<ClientRegistrationPublicInfo> getClientRegistrationPublicInfos() {
        return clientRegistrationWrapperService.getClientRegistrationPublicInfos();
    }

    @PostMapping
    public ClientRegistrationWrapper addIdpRegistration(
            @RequestPart ClientRegistrationWrapperDto clientRegistrationWrapperDto,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException { //TODO: add validator
        return clientRegistrationWrapperService.saveClientRegistrationWrapper(clientRegistrationWrapperDto, image);
    }

    @PatchMapping("/{id}")
    public ClientRegistrationWrapper updateIdpRegistration(
            @PathVariable String id,
            @RequestPart ClientRegistrationWrapperDto clientRegistrationWrapperDto,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        return clientRegistrationWrapperService.updateClientRegistrationWrapper(id, clientRegistrationWrapperDto, image);
    }

    @DeleteMapping("/{id}")
    public void deleteIdpRegistration(@PathVariable String id) {
        clientRegistrationWrapperService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAllIdpRegistrations() {
        clientRegistrationWrapperService.deleteAll();
    }
}
