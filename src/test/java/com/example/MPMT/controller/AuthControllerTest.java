// package com.example.MPMT.controller;

// import com.example.MPMT.model.Users;
// import com.example.MPMT.service.AuthService;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import org.springframework.boot.test.mock.mockito.MockBean; // Pour @MockBean
// import org.mockito.Mock; // Pour @Mock
// import org.mockito.InjectMocks; // Pour @InjectMocks
// import org.mockito.MockitoAnnotations; // Pour MockitoAnnotations.openMocks(this)

// import java.util.Optional;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(AuthController.class)
// public class AuthControllerTest {

// @Autowired
//     private MockMvc mockMvc;

//     @Mock
//     private AuthService authService; // Mock de Mockito

//     @InjectMocks
//     private AuthController authController; // Injection du mock

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this); // Initialisation des mocks
//     }

//     @Test
//     void login_ShouldReturnUser_WhenCredentialsAreValid() throws Exception {
//         // Configuration du mock
//         Users user = new Users(1L, "JohnDoe", "password123", "john@example.com");
//         when(authService.authenticate("john@example.com", "password123"))
//                 .thenReturn(Optional.of(user));

//         // Exécution de la requête et assertions
//         mockMvc.perform(post("/api/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"john@example.com\",\"password\":\"password123\"}"))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.id").value(1))
//             .andExpect(jsonPath("$.username").value("JohnDoe"))
//             .andExpect(jsonPath("$.email").value("john@example.com"));
//     }

//     @Test
//     void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
//         // Mock du service
//         when(authService.authenticate("john@example.com", "wrongpassword"))
//                 .thenReturn(Optional.empty());

//         // Requête et assertions
//         mockMvc.perform(post("/api/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"john@example.com\",\"password\":\"wrongpassword\"}"))
//             .andExpect(status().isUnauthorized())
//             .andExpect(content().string("Invalid credentials"));
//     }

//     @Test
//     void login_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
//         // Mock du service
//         when(authService.authenticate(Mockito.anyString(), Mockito.anyString()))
//                 .thenThrow(new RuntimeException("Service error"));

//         // Requête et assertions
//         mockMvc.perform(post("/api/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\":\"john@example.com\",\"password\":\"password123\"}"))
//             .andExpect(status().isInternalServerError())
//             .andExpect(content().string("An error occurred"));
//     }
// }
