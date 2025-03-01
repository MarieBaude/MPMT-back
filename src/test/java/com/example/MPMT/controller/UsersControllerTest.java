package com.example.MPMT.controller;

import com.example.MPMT.model.Users;
import com.example.MPMT.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build(); // Configuration de MockMvc
    }

    @Test
    void testGetAllUsers() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        when(usersService.findAll()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void testGetUsersById() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        when(usersService.getUsersById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }


    @Test
    void testGetUserByMail() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(usersService.getUserByMail("test@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/email/test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetUserByMailNotFound() throws Exception {
        when(usersService.getUserByMail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/email/test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(usersService.saveUser(any(Users.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}