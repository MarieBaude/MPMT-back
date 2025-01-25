package com.example.MPMT.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.MPMT.dto.AssignRoleDTO;
import com.example.MPMT.dto.GetAllProjectsFromUserDTO;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Users;
import com.example.MPMT.model.ProjectRole;
import com.example.MPMT.repository.ProjectRoleRepository;
import com.example.MPMT.repository.ProjectsRepository;
import com.example.MPMT.repository.UsersRepository;

public class ProjectsServiceTest {
    @Mock
    private ProjectsRepository projectsRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRoleRepository projectRoleRepository;

    @InjectMocks
    private ProjectsService projectsService;

    private Users user;
    private Projects project;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Création d'un utilisateur pour les tests
        user = new Users(1L, "John Doe", "password123", "john@example.com");

        // Créer un projet
        project = new Projects("Test Project", user, "Description", LocalDateTime.now());
    }

    @Test
    void testCreateProjectSuccess() {
        // Données d'entrée
        ProjectCreationDTO dto = new ProjectCreationDTO("Test Project", "Description", LocalDateTime.now(), 1L);

        // Mock du comportement
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectsRepository.save(any(Projects.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Exécution
        Projects savedProject = projectsService.createProject(dto);

        // Vérifications
        assertNotNull(savedProject);
        assertEquals("Test Project", savedProject.getName());
        verify(projectRoleRepository).save(any(ProjectRole.class));
    }

    @Test
    void testCreateProjectUserNotFound() {
        // Données d'entrée
        ProjectCreationDTO dto = new ProjectCreationDTO("Test Project", "Description", LocalDateTime.now(), 1L);

        // Mock du comportement
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérification de l'exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectsService.createProject(dto);
        });
        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void testGetProjectsByIdFound() {
        // Mock du comportement
        when(projectsRepository.findById(1L)).thenReturn(Optional.of(project));

        // Exécution
        Optional<Projects> foundProject = projectsService.getProjectsById(1L);

        // Vérifications
        assertTrue(foundProject.isPresent());
        assertEquals("Test Project", foundProject.get().getName());
    }

    @Test
    void testGetProjectsByIdNotFound() {
        // Mock du comportement
        when(projectsRepository.findById(1L)).thenReturn(Optional.empty());

        // Exécution
        Optional<Projects> foundProject = projectsService.getProjectsById(1L);

        // Vérification
        assertFalse(foundProject.isPresent());
    }

    @Test
    void testAssignRoleSuccess() {
        // Données d'entrée
        AssignRoleDTO dto = new AssignRoleDTO(1L, 1L, "ADMIN");

        // Mock du comportement
        when(projectsRepository.findById(1L)).thenReturn(Optional.of(project));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRoleRepository.findByProjectAndUser(project, user))
                .thenReturn(new ProjectRole(project, user, ProjectRole.Role.MEMBER));

        // Exécution
        projectsService.assignRole(dto);

        // Vérifications
        verify(projectRoleRepository).save(any(ProjectRole.class));
    }

    @Test
    void testAssignRoleUserNotAssigned() {
        // Données d'entrée
        AssignRoleDTO dto = new AssignRoleDTO(1L, 1L, "ADMIN");

        // Mock du comportement
        when(projectsRepository.findById(1L)).thenReturn(Optional.of(project));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRoleRepository.findByProjectAndUser(project, user)).thenReturn(null);

        // Vérification de l'exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            projectsService.assignRole(dto);
        });
        assertEquals("L'utilisateur n'est pas assigné au projet.", exception.getMessage());
    }

    @Test
    void testGetProjectsByUserIdSuccess() {
        // Mock du comportement
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectsRepository.findByCreatedByOrProjectRoles_User(user, user)).thenReturn(List.of(project));

        // Exécution
        List<GetAllProjectsFromUserDTO> projects = projectsService.getProjectsByUserId(1L);

        // Vérifications
        assertNotNull(projects);
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
    }

    @Test
    void testVerifyAdminRoleSuccess() {
        // Mock du comportement
        when(projectRoleRepository.findByProjectIdAndUserId(1L, 1L)).thenReturn(
                List.of(new ProjectRole(project, user, ProjectRole.Role.ADMIN)));

        // Exécution
        projectsService.verifyAdminRole(1L, 1L);

        // Vérifications
        verify(projectRoleRepository).findByProjectIdAndUserId(1L, 1L);
    }

    @Test
    void testVerifyAdminRoleNotAdmin() {
        // Mock du comportement
        when(projectRoleRepository.findByProjectIdAndUserId(1L, 1L)).thenReturn(
                List.of(new ProjectRole(project, user, ProjectRole.Role.MEMBER)));

        // Vérification de l'exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectsService.verifyAdminRole(1L, 1L);
        });
        assertEquals("Accès refusé, vous n'êtes pas ADMIN", exception.getMessage());
    }
}
