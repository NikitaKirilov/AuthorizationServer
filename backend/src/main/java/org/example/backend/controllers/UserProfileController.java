package org.example.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UpdateUserDto;
import org.example.backend.dtos.UpdateUserPasswordDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.services.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserDto getCurrentUser() {
        return userProfileService.getCurrentUserDto();
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return userProfileService.updateUser(updateUserDto);
    }

    @PutMapping("/password/update")
    public UserDto updatePassword(@RequestBody UpdateUserPasswordDto updateUserPasswordDto) {
        return userProfileService.updatePassword(updateUserPasswordDto);
    }

    @PutMapping("/email/update")
    public UserDto updateEmail(@RequestBody String email) {
        return userProfileService.updateEmail(email);
    }

    @PutMapping("/email/refresh")
    public void refreshCode() {
        userProfileService.refreshCode();
    }

    @PutMapping("/email/verify")
    public UserDto verifyNewEmail(@RequestParam String code) {
        return userProfileService.verifyNewEmail(code);
    }
}
