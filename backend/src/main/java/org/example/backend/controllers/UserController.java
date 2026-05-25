package org.example.backend.controllers;

import org.example.backend.dtos.UserDto;
import org.example.backend.models.entities.User;
import org.example.backend.services.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/*@RestController
@RequestMapping("/users")*/
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers(User user, Pageable pageable) {
        return userService.getAllUsers(user, pageable); //TODO to AdminUserContoller
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return userService.getUserDtoById(id); //TODO to AdminUserContoller
    }
}
