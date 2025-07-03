package com.bitlab.project.service;

import com.bitlab.project.model.entity.User;
import com.bitlab.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;


    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "user1", "user1@example.com"),
                new User(2L, "user2", "user2@example.com")
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUsersByEmailAndUsername() {
        User mockUser = new User(1L, "john", "john@example.com");
        when(userRepository.findByEmailAndUsername("john@example.com", "john"))
                .thenReturn(List.of(mockUser));

        List<User> result = userService.getUsersByEmailAndUsername("john@example.com", "john");

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
        verify(userRepository, times(1))
                .findByEmailAndUsername("john@example.com", "john");
    }
}
