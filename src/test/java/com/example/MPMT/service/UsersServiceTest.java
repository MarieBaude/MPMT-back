package com.example.MPMT.service;

import com.example.MPMT.model.Users;
import com.example.MPMT.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private Users user;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);
        
        // Création d'un utilisateur pour les tests
        user = new Users(1L, "John Doe", "password123", "john@example.com");
    }

    @Test
    void testFindAll() {
        // Configurer le mock pour retourner une liste d'utilisateurs
        when(usersRepository.findAll()).thenReturn(Arrays.asList(user));
        
        // Exécuter le test
        List<Users> usersList = usersService.findAll();
        
        // Vérifier les résultats
        assertNotNull(usersList);
        assertEquals(1, usersList.size());
        assertEquals("John Doe", usersList.get(0).getUsername());
    }

    @Test
    void testGetUsersByIdFound() {
        // Configurer le mock pour retourner un utilisateur lorsque son ID est trouvé
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        // Exécuter le test
        Optional<Users> foundUser = usersService.getUsersById(1L);

        // Vérifier que l'utilisateur est bien trouvé
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getUsername());
    }

    @Test
    void testGetUsersByIdNotFound() {
        // Configurer le mock pour retourner un Optional vide si l'utilisateur n'est pas trouvé
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        // Exécuter le test
        Optional<Users> foundUser = usersService.getUsersById(1L);

        // Vérifier que l'utilisateur n'est pas trouvé
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testGetUserByMailFound() {
        // Configurer le mock pour retourner un utilisateur lorsqu'un email est trouvé
        when(usersRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // Exécuter le test
        Optional<Users> foundUser = usersService.getUserByMail("john@example.com");

        // Vérifier que l'utilisateur est trouvé par email
        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void testGetUserByMailNotFound() {
        // Configurer le mock pour retourner un Optional vide si l'email n'est pas trouvé
        when(usersRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        // Exécuter le test
        Optional<Users> foundUser = usersService.getUserByMail("john@example.com");

        // Vérifier que l'utilisateur n'est pas trouvé
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUser() {
        // Configurer le mock pour simuler l'enregistrement d'un utilisateur
        when(usersRepository.save(user)).thenReturn(user);

        // Exécuter le test
        Users savedUser = usersService.saveUser(user);

        // Vérifier que l'utilisateur a bien été sauvegardé
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getUsername());
    }
}
