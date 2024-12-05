package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.ClientRegistrationWrapperDto;
import org.example.backend.exceptions.ClientRegistrationAlreadyExistsException;
import org.example.backend.exceptions.ClientRegistrationNotFoundException;
import org.example.backend.models.ClientRegistrationPublicInfo;
import org.example.backend.models.entities.ClientRegistrationWrapper;
import org.example.backend.repositories.ClientRegistrationWrapperRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.example.backend.utils.FileUtils.validatePngImage;
import static org.example.backend.utils.StringUtils.getAuthorizationRequestUri;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;

@Service
@RequiredArgsConstructor
public class ClientRegistrationWrapperService {

    private final ClientRegistrationWrapperRepository clientRegistrationWrapperRepository;

    public ClientRegistrationWrapper getById(String id) {
        return clientRegistrationWrapperRepository.findById(id).orElseThrow(() ->
                new ClientRegistrationNotFoundException("Client registration not found by id: " + id)
        );
    }

    public ClientRegistrationWrapper getByRegistrationId(String registrationId) {
        return clientRegistrationWrapperRepository.findByRegistrationId(registrationId).orElseThrow(() ->
                new ClientRegistrationNotFoundException("Client registration not found by registrationId: " + registrationId)
        );
    }

    public Optional<ClientRegistrationWrapper> getOptionalByRegistrationId(String registrationId) {
        return clientRegistrationWrapperRepository.findByRegistrationId(registrationId);
    }

    public List<ClientRegistrationWrapper> getAll() {
        return clientRegistrationWrapperRepository.findAll();
    }

    public List<ClientRegistrationPublicInfo> getClientRegistrationPublicInfos() {
        return getAll().stream()
                .map(idpRegistration -> new ClientRegistrationPublicInfo(
                        idpRegistration.getRegistrationId(),
                        idpRegistration.getClientName(),
                        getAuthorizationRequestUri(idpRegistration.getRegistrationId()),
                        idpRegistration.getImage()
                ))
                .sorted(Comparator.comparing(ClientRegistrationPublicInfo::getClientName))
                .toList();
    }

    public ClientRegistrationWrapper saveClientRegistrationWrapper(
            ClientRegistrationWrapperDto clientRegistrationWrapperDto,
            MultipartFile image
    ) throws IOException {
        validateRegistrationId(clientRegistrationWrapperDto.getRegistrationId());
        validatePngImage(image);

        ClientRegistrationWrapper clientRegistrationWrapper = clientRegistrationWrapperDto.toEntity()
                .setId(randomUUID().toString())
                .setImage(image.getBytes())
                .setCreatedAt(getCurrentTimestamp())
                .setUpdatedAt(getCurrentTimestamp());

        return clientRegistrationWrapperRepository.save(clientRegistrationWrapper);
    }

    public ClientRegistrationWrapper updateClientRegistrationWrapper(
            String clintRegistrationId,
            ClientRegistrationWrapperDto clientRegistrationWrapperDto,
            MultipartFile image
    ) throws IOException {
        ClientRegistrationWrapper existingClientRegistrationWrapper = getById(clintRegistrationId);
        String newRegistrationId = clientRegistrationWrapperDto.getRegistrationId();
        String existingRegistrationId = existingClientRegistrationWrapper.getRegistrationId();

        if (!newRegistrationId.equals(existingRegistrationId)) {
            validateRegistrationId(newRegistrationId);
        }

        validatePngImage(image);

        ClientRegistrationWrapper updatedClientRegistrationWrapper = clientRegistrationWrapperDto.toEntity()
                .setId(existingClientRegistrationWrapper.getId())
                .setImage(image.getBytes())
                .setCreatedAt(existingClientRegistrationWrapper.getCreatedAt())
                .setUpdatedAt(getCurrentTimestamp());

        return clientRegistrationWrapperRepository.save(updatedClientRegistrationWrapper);
    }

    private void validateRegistrationId(String registrationId) {
        Optional<ClientRegistrationWrapper> clientRegistrationWrapper = getOptionalByRegistrationId(registrationId);

        if (clientRegistrationWrapper.isPresent()) {
            throw new ClientRegistrationAlreadyExistsException("Client registration with id: '" + registrationId + "' already exists");
        }
    }

    public void deleteById(String id) {
        //TODO: SessionService.logoutIdpRegistrationUsers(IdpRegistration idpRegistration)
        clientRegistrationWrapperRepository.deleteById(id);
    }

    public void deleteAll() {
        clientRegistrationWrapperRepository.findAll().forEach(
                clientRegistrationWrapper ->
                        clientRegistrationWrapperRepository.deleteById(clientRegistrationWrapper.getId())
                //TODO: SessionService.logoutIdpRegistrationUsers(IdpRegistration idpRegistration)
        );
    }
}
