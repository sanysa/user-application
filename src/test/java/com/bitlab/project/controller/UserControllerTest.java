package com.bitlab.project.controller;

import com.bitlab.project.model.entity.User;
import com.bitlab.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockServiceConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @com.bitlab.project.controller.TestConfiguration
    static class MockServiceConfig {
        @Bean
        public UserService userService() {
            return mock(UserService.class);
        }
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "alice", "alice@example.com"),
                new User(2L, "bob", "bob@example.com")
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("alice"));
    }

    @Test
    void testSearchByEmailAndUsername() throws Exception {
        User mockUser = new User(1L, "john", "john@example.com");

        when(userService.getUsersByEmailAndUsername("john@example.com", "john"))
                .thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/users/search")
                        .param("email", "john@example.com")
                        .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }
}
