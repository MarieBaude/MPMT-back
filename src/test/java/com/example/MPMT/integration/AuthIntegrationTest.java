package com.example.MPMT.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.MPMT.controller.AuthController;
import com.example.MPMT.dto.LoginDTO;
import com.example.MPMT.model.Users;
import com.example.MPMT.service.AuthService;
import com.example.MPMT.service.DataGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(AuthController.class)
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean
    private AuthService authService; 

    @MockBean
    private DataGeneratorService dataGeneratorService;

    @Test
    void testLoginSuccess() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        Users user = new Users();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        when(authService.authenticate(anyString(), anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com")); 
    }
}