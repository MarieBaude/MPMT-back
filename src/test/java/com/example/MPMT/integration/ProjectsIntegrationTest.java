package com.example.MPMT.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.MPMT.controller.ProjectsController;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Users;
import com.example.MPMT.service.DataGeneratorService;
import com.example.MPMT.service.ProjectsService;
import com.example.MPMT.service.UsersService;

@WebMvcTest(ProjectsController.class) 
public class ProjectsIntegrationTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean
    private ProjectsService projectsService; 

    @MockBean
    private UsersService usersService; 

    @MockBean 
    private DataGeneratorService dataGeneratorService;

    @Test
    void testGetAllProjects() throws Exception {
        // Données de test
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        Projects project1 = new Projects("Project 1", user, "Description 1", LocalDateTime.now());
        Projects project2 = new Projects("Project 2", user, "Description 2", LocalDateTime.now());
        List<Projects> projects = Arrays.asList(project1, project2);

        // Simuler la réponse du service
        when(projectsService.findAll()).thenReturn(projects);

        // Exécuter la requête HTTP
        mockMvc.perform(get("/api/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$[0].name").value("Project 1")) 
                .andExpect(jsonPath("$[1].name").value("Project 2")); 
    }

    @Test
    void testCreateProject() throws Exception {
        // Données d'entrée
        ProjectCreationDTO dto = new ProjectCreationDTO();
        dto.setName("New Project");
        dto.setDescription("Description");
        dto.setCreatedAt(LocalDateTime.now());
        dto.setCreatedById(1L);

        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        Projects project = new Projects("New Project", user, "Description", LocalDateTime.now());
        when(projectsService.createProject(any(ProjectCreationDTO.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Project\",\"description\":\"Description\",\"createdAt\":\"2023-10-01T12:00:00\",\"createdById\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Project")); 
    }

    @Test
    void testGetProjectById() throws Exception {
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        Projects project = new Projects("Project 1", user, "Description", LocalDateTime.now());
        when(projectsService.getProjectsById(1L)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/projects/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.name").value("Project 1")); 
    }
}