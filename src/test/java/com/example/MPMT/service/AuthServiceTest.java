package com.example.MPMT.service;

import com.example.MPMT.model.Users;
import com.example.MPMT.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private UsersRepository usersRepository; // Mock du repository

    @InjectMocks
    private AuthService authService; // Le service à tester

    private Users user;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);
        
        // Création d'un utilisateur pour les tests
        user = new Users(1L, "John Doe", "password123", "john@example.com");
    }

    @Test
    void testAuthenticate_UserExistsAndCorrectPassword() {
        // Configurer le mock pour retourner un utilisateur trouvé par email
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        
        // Exécuter le test : authentification avec un mot de passe correct
        Optional<Users> authenticatedUser = authService.authenticate(user.getEmail(), "password123");

        // Vérification que l'utilisateur est bien authentifié
        assertTrue(authenticatedUser.isPresent());
        assertEquals(user.getEmail(), authenticatedUser.get().getEmail());
    }

    @Test
    void testAuthenticate_UserExistsButIncorrectPassword() {
        // Configurer le mock pour retourner un utilisateur trouvé par email
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        
        // Exécuter le test : authentification avec un mot de passe incorrect
        Optional<Users> authenticatedUser = authService.authenticate(user.getEmail(), "wrongPassword");

        // Vérification que l'authentification échoue
        assertFalse(authenticatedUser.isPresent());
    }

    @Test
    void testAuthenticate_UserDoesNotExist() {
        // Configurer le mock pour retourner un utilisateur absent (vide)
        when(usersRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        
        // Exécuter le test : tentative d'authentification pour un utilisateur qui n'existe pas
        Optional<Users> authenticatedUser = authService.authenticate(user.getEmail(), "password123");

        // Vérification que l'utilisateur n'est pas authentifié
        assertFalse(authenticatedUser.isPresent());
    }
}
