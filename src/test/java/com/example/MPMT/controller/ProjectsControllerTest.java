package com.example.MPMT.controller;

import com.example.MPMT.dto.*;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Users;
import com.example.MPMT.service.ProjectsService;
import com.example.MPMT.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectsService projectService;

    @Mock
    private UsersService userService;

    @InjectMocks
    private ProjectsController projectsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
        mockMvc = MockMvcBuilders.standaloneSetup(projectsController).build(); // Configuration de MockMvc
    }

    @Test
    void testGetAllUsers() throws Exception {
        Projects project = new Projects();
        project.setId(1L);
        project.setName("Test Project");

        when(projectService.findAll()).thenReturn(Collections.singletonList(project));

        mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Project"));
    }

    @Test
    void testCreateProject() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now(); // Simuler une date de création

        Projects project = new Projects();
        project.setId(1L);
        project.setName("New Project");

        when(projectService.createProject(any(ProjectCreationDTO.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Project\",\"description\":\"Project Description\",\"createdAt\":\""
                        + createdAt + "\",\"createdById\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void testGetProjectById() throws Exception {
        Projects project = new Projects();
        project.setId(1L);
        project.setName("Test Project");

        when(projectService.getProjectsById(1L)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void testChangeUserRole() throws Exception {
        AssignRoleDTO dto = new AssignRoleDTO();
        dto.setUserId(2L);
        dto.setRole("MEMBER");

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rôle mis à jour avec succès.");

        doNothing().when(projectService).verifyAdminRole(1L, 1L);
        doNothing().when(projectService).assignRole(any(AssignRoleDTO.class));

        mockMvc.perform(patch("/api/projects/1/change-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":2,\"role\":\"MEMBER\"}")
                .param("currentUserId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rôle mis à jour avec succès."));
    }

    @Test
    void testGetProjectsByUserId() throws Exception {
        GetAllProjectsFromUserDTO dto = new GetAllProjectsFromUserDTO();
        dto.setId(1L);
        dto.setName("Test Project");

        when(projectService.getProjectsByUserId(1L)).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/projects/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Project"));
    }

    @Test
    void testInviteMemberToProject() throws Exception {
        InviteMemberDTO request = new InviteMemberDTO();
        request.setEmail("test@example.com");
        request.setRole("MEMBER");

        Projects project = new Projects();
        project.setId(1L);
        project.setName("Test Project");

        Users invitedUser = new Users();
        invitedUser.setId(2L);
        invitedUser.setEmail("test@example.com");

        when(projectService.getProjectsById(1L)).thenReturn(Optional.of(project));
        when(userService.getUserByMail("test@example.com")).thenReturn(Optional.of(invitedUser));
        doNothing().when(projectService).verifyAdminRole(1L, 1L);
        doNothing().when(projectService).addUserToProject(any(Projects.class), any(Users.class), anyString());

        mockMvc.perform(post("/api/projects/1/invite")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"role\":\"MEMBER\"}")
                .param("currentUserId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void testGetProjectMembers() throws Exception {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        when(projectService.getProjectMembers(1L)).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/projects/1/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }
}