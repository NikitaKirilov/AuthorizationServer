package org.example.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDetailsDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.services.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserDetailsDto getCurrentUserDetailsDto() {
        return userProfileService.getCurrentUserDetailsDto();
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
        return userProfileService.updateUser(userDto);
    }

    @PutMapping("/password")
    public void updatePassword(@Valid @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        userProfileService.updatePassword(userPasswordUpdateDto);
    }
}
