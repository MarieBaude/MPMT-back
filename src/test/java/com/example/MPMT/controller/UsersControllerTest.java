package com.example.MPMT.controller;

import com.example.MPMT.model.Users;
import com.example.MPMT.service.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersService usersService;

    @Test
    void testGetAllUsers() throws Exception {
        // Mock service response
        Users user1 = new Users(1L, "John Doe", "password123", "john@example.com");
        Users user2 = new Users(1L, "Jane Doe", "password456", "jane@example.com");
        when(usersService.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Perform GET request
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("John Doe"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        // Mock service response
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        when(usersService.getUsersById(1L)).thenReturn(Optional.of(user));

        // Perform GET request
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserByMail_Found() throws Exception {
        // Mock service response
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        when(usersService.getUserByMail("john@example.com")).thenReturn(Optional.of(user));

        // Perform GET request
        mockMvc.perform(get("/api/users/email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserByMail_NotFound() throws Exception {
        // Mock service response
        when(usersService.getUserByMail("unknown@example.com")).thenReturn(Optional.empty());

        // Perform GET request
        mockMvc.perform(get("/api/users/email/unknown@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        // Mock service response
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        when(usersService.saveUser(Mockito.any(Users.class))).thenReturn(user);

        // Perform POST request
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"John Doe\", \"email\": \"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}
