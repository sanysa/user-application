package com.bitlab.project.controller;

import com.bitlab.project.model.dto.UserDto;
import com.bitlab.project.model.entity.User;
import com.bitlab.project.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public List<User> searchByEmailAndUsername(@RequestParam String email,
                                               @RequestParam String username) {
        return userService.getUsersByEmailAndUsername(email, username);
    }

    @PostMapping
    public void save(@RequestBody UserDto dto) {
        userService.save(dto);
    }


}