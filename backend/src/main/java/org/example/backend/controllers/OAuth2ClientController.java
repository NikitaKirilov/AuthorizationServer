package org.example.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.OAuth2ClientDto;
import org.example.backend.services.OAuth2ClientService;
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
@RequestMapping("/oauth2/clients")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientService oAuth2ClientService;

    @GetMapping
    public Page<OAuth2ClientDto> getAllOAuth2Clients(OAuth2ClientDto oAuth2ClientDto, Pageable pageable) {
        return oAuth2ClientService.getAllOAuth2Clients(oAuth2ClientDto, pageable);
    }

    @GetMapping("/{clientId}")
    public OAuth2ClientDto getOAuth2ClientByClientId(@PathVariable String clientId) {
        return oAuth2ClientService.getOAuth2ClientByClientId(clientId);
    }

    @PostMapping
    public OAuth2ClientDto createOrUpdate(@Valid @RequestBody OAuth2ClientDto oAuth2ClientDto) {
        return oAuth2ClientService.createOrUpdate(oAuth2ClientDto);
    }

    @PutMapping("/secret/{clientId}")
    public String generateSecret(@PathVariable String clientId) {
        return oAuth2ClientService.generateClientSecret(clientId);
    }

    @DeleteMapping("/{clientId}")
    public void delete(@PathVariable String clientId) {
        oAuth2ClientService.deleteOAuth2Client(clientId);
    }
}
