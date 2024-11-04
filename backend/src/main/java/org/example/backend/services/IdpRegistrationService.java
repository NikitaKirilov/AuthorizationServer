package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.IdpRegistrationDto;
import org.example.backend.exceptions.idpregistrations.IdpRegistrationAlreadyExistsException;
import org.example.backend.exceptions.idpregistrations.IdpRegistrationNotFoundException;
import org.example.backend.models.CustomOAuth2User;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.repositories.IdpRegistrationRepository;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;

@Service
@RequiredArgsConstructor
public class IdpRegistrationService {

    private final IdpRegistrationRepository idpRegistrationRepository;
    private final SessionRegistry sessionRegistry;

    public IdpRegistration getById(String id) {
        return idpRegistrationRepository.findById(id)
                .orElseThrow(() -> new IdpRegistrationNotFoundException(id));
    }

    public IdpRegistration getByRegistrationId(String registrationId) {
        return idpRegistrationRepository.findByRegistrationId(registrationId)
                .orElseThrow(() -> new IdpRegistrationNotFoundException(registrationId));
    }

    public Optional<IdpRegistration> getOptionalByRegistrationId(String registrationId) {
        return idpRegistrationRepository.findByRegistrationId(registrationId);
    }

    public List<IdpRegistration> getAll() {
        return idpRegistrationRepository.findAll();
    }

    public IdpRegistration saveIdpRegistration(IdpRegistrationDto idpRegistrationDto) {
        checkRegistrationId(idpRegistrationDto.getRegistrationId());

        IdpRegistration idpRegistration = idpRegistrationDto.toEntity();
        idpRegistration.setId(UUID.randomUUID().toString());
        idpRegistration.setCreatedAt(getCurrentTimestamp());
        idpRegistration.setUpdatedAt(getCurrentTimestamp());

        return idpRegistrationRepository.save(idpRegistration);
    }

    public IdpRegistration updateIdpRegistration(String id, IdpRegistrationDto idpRegistrationDto) {
        IdpRegistration existingIdpRegistration = this.getById(id);
        String newRegistrationId = idpRegistrationDto.getRegistrationId();
        String existingRegistrationId = existingIdpRegistration.getRegistrationId();

        if (!newRegistrationId.equals(existingRegistrationId)) {
            checkRegistrationId(newRegistrationId);
        }

        IdpRegistration updatedIdpRegistration = idpRegistrationDto.toEntity();

        existingIdpRegistration.setRegistrationId(newRegistrationId);
        existingIdpRegistration.setClientId(updatedIdpRegistration.getClientId());
        existingIdpRegistration.setClientSecret(existingIdpRegistration.getClientSecret());
        existingIdpRegistration.setImageUri(existingIdpRegistration.getImageUri());
        existingIdpRegistration.setUpdatedAt(getCurrentTimestamp());

        return idpRegistrationRepository.save(existingIdpRegistration);
    }

    private void checkRegistrationId(String registrationId) {
        Optional<IdpRegistration> idpRegistration = this.getOptionalByRegistrationId(registrationId);

        if (idpRegistration.isPresent()) {
            throw new IdpRegistrationAlreadyExistsException(registrationId);
        }
    }

    public void deleteById(String id) {
        IdpRegistration idpRegistration = this.getById(id);
        logoutAllUsers(idpRegistration.getRegistrationId());
        idpRegistrationRepository.deleteById(id);
    }

    public void deleteAll() {
        idpRegistrationRepository.findAll().forEach(
                idpRegistration -> {
                    idpRegistrationRepository.deleteById(idpRegistration.getId());
                    logoutAllUsers(idpRegistration.getRegistrationId());
                }
        );
    }

    private void logoutAllUsers(String registrationId) {
        List<SessionInformation> list = sessionRegistry.getAllPrincipals().stream()
                .filter(OAuth2AuthenticationToken.class::isInstance)
                .map(OAuth2AuthenticationToken.class::cast)
                .map(OAuth2AuthenticationToken::getPrincipal)
                .filter(CustomOAuth2User.class::isInstance)
                .map(CustomOAuth2User.class::cast)
                .filter(user -> user.getIdpRegistrationId().equals(registrationId))
                .map(user -> sessionRegistry.getAllSessions(user, false))
                .flatMap(List::stream)
                .toList();
        //TODO: move to session or security service
        list.forEach(SessionInformation::expireNow);
    }
}
