package org.example.backend.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.models.entities.User;
import org.example.backend.models.entities.UserDevice;
import org.example.backend.repositories.UserDeviceRepository;
import org.example.backend.utils.RequestUtils;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.USER_AGENT;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeviceService {

    private static final String DELIMITER = ", ";
    private static final String UNKNOWN_LOCATION = "Unknown Location";
    private static final String UNKNOWN_DETAILS = "Unknown Device";
    public static final String DEVICE_FORMAT = "%s %s - %s %s";

    private final DatabaseReader databaseReader;
    private final EmailService emailService;
    private final Parser userAgentParser;
    private final UserDeviceRepository userDeviceRepository;

    public void verifyDevice(User user, HttpServletRequest request) {
        String ip = RequestUtils.getIpAddress(request);
        String location = this.getIpLocation(ip);
        String details = this.getDeviceDetails(request.getHeader(USER_AGENT));

        userDeviceRepository.findByUserAndDetailsAndLocation(user, details, location).ifPresentOrElse(
                existingDevice -> {
                    existingDevice.setLastLoggedAt(Instant.now());
                    userDeviceRepository.save(existingDevice);
                },
                () -> {
                    UserDevice newDevice = new UserDevice();

                    newDevice.setId(UUID.randomUUID().toString());
                    newDevice.setUser(user);
                    newDevice.setDetails(details);
                    newDevice.setLocation(location);
                    newDevice.setLastLoggedAt(Instant.now());

                    userDeviceRepository.save(newDevice);
                    emailService.sendNewDeviceNotification(user, newDevice);
                }
        );
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
                    client.userAgent.family, client.userAgent.major,
                    client.os.family, client.os.major
            );
        }

        return details;
    }
}
