package org.example.backend.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dtos.UserDeviceDto;
import org.example.backend.exceptions.ForbiddenOperationException;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.repositories.UserDeviceRepository;
import org.example.backend.utils.RequestUtils;
import org.example.backend.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.USER_AGENT;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeviceService {

    private static final String DELIMITER = ", ";
    private static final String UNKNOWN_LOCATION = "Unknown Location";
    private static final String UNKNOWN_DETAILS = "Unknown Device";
    public static final String DEVICE_FORMAT = "%s - %s";

    private final DatabaseReader databaseReader;
    private final EmailService emailService;
    private final Parser userAgentParser;
    private final UserDeviceRepository userDeviceRepository;
    private final UserService userService;
    private final SessionService sessionService;
    private final AuthorizationService authorizationService;

    public List<UserDeviceDto> getUserDevices(Pageable pageable) {
        User user = userService.getCurrentUser();
        List<UserDevice> devices = userDeviceRepository.findAllByUser(user, pageable);
        String currentDeviceId = SecurityUtils.getCurrentDeviceId();

        return devices.stream()
                .map(device -> {
                    String deviceId = device.getId();

                    UserDeviceDto userDeviceDto = new UserDeviceDto();
                    userDeviceDto.setId(deviceId);
                    userDeviceDto.setLocation(device.getLocation());
                    userDeviceDto.setDetails(device.getDetails());
                    userDeviceDto.setCurrent(Objects.equals(currentDeviceId, deviceId));
                    userDeviceDto.setLastLoggedAt(device.getLastLoggedAt());

                    return userDeviceDto;
                })
                .toList();
    }

    public UserDevice saveAndVerifyDevice(User user, HttpServletRequest request) {
        String ip = RequestUtils.getIpAddress(request);
        String location = this.getIpLocation(ip);
        String details = this.getDeviceDetails(request.getHeader(USER_AGENT));

        UserDevice device;
        Optional<UserDevice> optional = userDeviceRepository.findByUserAndDetailsAndLocation(user, details, location);
        if (optional.isPresent()) {
            device = optional.get();
            device.setLastLoggedAt(Instant.now());
        } else {
            device = new UserDevice();
            device.setId(UUID.randomUUID().toString());
            device.setUser(user);
            device.setDetails(details);
            device.setLocation(location);
            device.setLastLoggedAt(Instant.now());

            emailService.sendNewDeviceNotification(user, device);
        }

        return userDeviceRepository.save(device);
    }

    @Transactional
    public void logoutDevice(String deviceId) {
        User user = userService.getCurrentUser();
        UserDevice device = userDeviceRepository.findById(deviceId)
                .orElseThrow();

        if (!Objects.equals(user, device.getUser())) {
            throw new ForbiddenOperationException("You are not allowed to manage this device");
        }

        sessionService.deleteSessionsByUserAndDevice(user, device);
        authorizationService.deleteAuthorizationsByUserAndDevice(user, device);
        userDeviceRepository.delete(device);
    }

    private String getIpLocation(String ip) {
        String location = UNKNOWN_LOCATION;

        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse cityResponse = databaseReader.city(ipAddress);

            String country = cityResponse.country().name();
            String city = cityResponse.city().name();

            if (country != null && city != null) {
                location = String.join(DELIMITER, country, city);
            }
        } catch (IOException | GeoIp2Exception e) {
            log.warn("Failed fetching ip location for ip: {}.", ip, e);
        }

        return location;
    }

    private String getDeviceDetails(String userAgent) {
        String details = UNKNOWN_DETAILS;
        Client client = userAgentParser.parse(userAgent);

        if (client != null) {
            details = DEVICE_FORMAT.formatted(
                    client.userAgent.family, client.os.family
            );
        }

        return details;
    }
}
