package org.example.backend.services;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.IdpRegistrationDto;
import org.example.backend.exceptions.FileValidationException;
import org.example.backend.exceptions.IdpRegistrationAlreadyExistsException;
import org.example.backend.exceptions.IdpRegistrationNotFoundException;
import org.example.backend.models.ClientRegistrationPublicInfo;
import org.example.backend.models.entities.IdpRegistration;
import org.example.backend.repositories.IdpRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.backend.utils.FileUtils.isPngSignatureValid;
import static org.example.backend.utils.StringUtils.getAuthorizationRequestUri;
import static org.example.backend.utils.TimestampUtils.getCurrentTimestamp;

@Service
@RequiredArgsConstructor
public class IdpRegistrationService {

    private static final long MAX_FILE_SIZE = 1024L * 1024L;

    private final IdpRegistrationRepository idpRegistrationRepository;

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

    public List<ClientRegistrationPublicInfo> getClientRegistrationPublicInfos() {
        return this.getAll().stream()
                .map(idpRegistration -> new ClientRegistrationPublicInfo(
                        idpRegistration.getRegistrationId(),
                        idpRegistration.getClientName(),
                        getAuthorizationRequestUri(idpRegistration.getRegistrationId()),
                        idpRegistration.getImage()
                )).toList();
    }

    public IdpRegistration saveIdpRegistration(IdpRegistrationDto idpRegistrationDto, MultipartFile image) throws IOException {
        checkRegistrationId(idpRegistrationDto.getRegistrationId());

        validateImage(image);

        IdpRegistration idpRegistration = idpRegistrationDto.toEntity();
        idpRegistration.setId(UUID.randomUUID().toString());
        idpRegistration.setImage(image.getBytes());
        idpRegistration.setCreatedAt(getCurrentTimestamp());
        idpRegistration.setUpdatedAt(getCurrentTimestamp());

        return idpRegistrationRepository.save(idpRegistration);
    }

    public IdpRegistration updateIdpRegistration(String id, IdpRegistrationDto idpRegistrationDto, MultipartFile image) throws IOException {
        IdpRegistration existingIdpRegistration = this.getById(id);
        String newRegistrationId = idpRegistrationDto.getRegistrationId();
        String existingRegistrationId = existingIdpRegistration.getRegistrationId();

        if (!newRegistrationId.equals(existingRegistrationId)) {
            checkRegistrationId(newRegistrationId);
        }

        validateImage(image);

        IdpRegistration updatedIdpRegistration = idpRegistrationDto.toEntity();
        existingIdpRegistration.setRegistrationId(newRegistrationId);
        existingIdpRegistration.setClientId(updatedIdpRegistration.getClientId());
        existingIdpRegistration.setClientSecret(existingIdpRegistration.getClientSecret());
        existingIdpRegistration.setImage(image.getBytes());
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
        //TODO: SessionService.logoutIdpRegistrationUsers(IdpRegistration idpRegistration)
        idpRegistrationRepository.deleteById(id);
    }

    public void deleteAll() {
        idpRegistrationRepository.findAll().forEach(
                idpRegistration -> {
                    idpRegistrationRepository.deleteById(idpRegistration.getId());
                    //TODO: SessionService.logoutIdpRegistrationUsers(IdpRegistration idpRegistration)
                }
        );
    }

    private void validateImage(MultipartFile file) throws IOException {
        if (file == null) {
            return;
        }

        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("File size must be less then 1mb");
        }

        if (!isPngSignatureValid(file.getBytes())) {
            throw new FileValidationException("File is not a PNG file");
        }
    }
}
