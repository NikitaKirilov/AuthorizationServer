package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.configs.client.JdbcClientRegistrationRepository;
import org.example.backend.dtos.ClientRegistrationDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client-registrations")
@RequiredArgsConstructor
public class ClientRegistrationController {

    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    @PostMapping
    public void addClientRegistration(@RequestBody ClientRegistrationDto clientRegistrationDto) { //TODO: add validator
        clientRegistrationRepository.save(clientRegistrationDto);
    }
}
