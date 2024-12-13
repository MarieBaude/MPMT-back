package com.example.MPMT.controller;

import com.example.MPMT.model.Users;
import com.example.MPMT.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() throws Exception {
        // Préparation des données
        Users user = new Users(1L, "JohnDoe", "password123", "john@example.com");

        // Mock du service
        when(authService.authenticate("john@example.com", "password123"))
                .thenReturn(Optional.of(user));

        // Requête et assertions
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("JohnDoe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
        // Mock du service
        when(authService.authenticate("john@example.com", "wrongpassword"))
                .thenReturn(Optional.empty());

        // Requête et assertions
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\",\"password\":\"wrongpassword\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void login_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        // Mock du service
        when(authService.authenticate(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Service error"));

        // Requête et assertions
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@example.com\",\"password\":\"password123\"}"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("An error occurred"));
    }
}
