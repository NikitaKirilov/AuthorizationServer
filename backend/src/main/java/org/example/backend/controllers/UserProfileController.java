package org.example.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dtos.UserDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.services.UserProfileService;
import org.springframework.security.core.Authentication;
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
    public Object getCurrentUser(Authentication authentication) { //TODO return dto instead of authentication principal
        return authentication.getPrincipal();
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userProfileService.updateUser(userUpdateDto);
    }

    @PutMapping("/password")
    public UserDto updatePassword(@Valid @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        return userProfileService.updatePassword(userPasswordUpdateDto);
    }
}
