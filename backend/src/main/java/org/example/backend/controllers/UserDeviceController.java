package org.example.backend.controllers;

import org.example.backend.dtos.UserDeviceDto;
import org.example.backend.services.UserDeviceService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/devices")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    public UserDeviceController(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    @GetMapping
    public List<UserDeviceDto> getUserDevices() {
        return userDeviceService.getUserDevices();
    }

    @DeleteMapping("/logout/{deviceId}")
    public void logoutDevice(@PathVariable String deviceId) {
        userDeviceService.logoutDevice(deviceId);
    }
}
