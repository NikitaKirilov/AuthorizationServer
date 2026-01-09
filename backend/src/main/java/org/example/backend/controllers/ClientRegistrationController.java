package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/idp-registrations")
@RequiredArgsConstructor
public class ClientRegistrationController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/{id}")
    public ClientRegistration getIdpRegistration(@PathVariable String id) {
        return clientRegistrationRepository.findByRegistrationId(id);
    }
}
