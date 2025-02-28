// package com.example.MPMT.controller;

// import com.example.MPMT.dto.*;
// import com.example.MPMT.model.Projects;
// import com.example.MPMT.model.Users;
// import com.example.MPMT.service.ProjectsService;
// import com.example.MPMT.service.UsersService;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.time.LocalDateTime;
// import java.util.*;

// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(ProjectsController.class)
// class ProjectsControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private ProjectsService projectService;

//     @MockBean
//     private UsersService usersService;

//     @Test
//     void testCreateProject() throws Exception {
//         // Données fictives pour le projet retourné
//         Projects project = new Projects();
//         project.setId(1L); // ID fictif
//         project.setName("Projet Test");
//         project.setDescription("Description test");
    
//         // Mock service response
//         when(projectService.createProject(Mockito.any(ProjectCreationDTO.class))).thenReturn(project);
    
//         // Perform POST request
//         mockMvc.perform(post("/api/projects/create")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{\"name\": \"Projet Test\", \"description\": \"Description test\", \"createdById\": 1}"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1)) 
//                 .andExpect(jsonPath("$.name").value("Projet Test"))
//                 .andExpect(jsonPath("$.description").value("Description test")); 
//     }

//     @Test
//     void testGetProjectById() throws Exception {
//         // Créer un utilisateur pour createdBy
//         Users createdByUser = new Users(1L, "John Doe", "password123", "john@example.com");

//         // Créer un objet Projects
//         LocalDateTime createdAt = LocalDateTime.now();
//         Projects project = new Projects("Project Name", createdByUser, "Description", createdAt);

//         // Mock service response
//         when(projectService.getProjectsById(1L)).thenReturn(Optional.of(project));

//         // Perform GET request
//         mockMvc.perform(get("/api/projects/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Project Name"))
//                 .andExpect(jsonPath("$.description").value("Description"));
//     }

//     @Test
//     void testChangeUserRole() throws Exception {
//         // Mock service response
//         AssignRoleDTO dto = new AssignRoleDTO(1L, 2L, "ADMIN");
//         doNothing().when(projectService).verifyAdminRole(1L, 1L); // Vérifier le rôle admin
//         doNothing().when(projectService).assignRole(dto); // Assigner le rôle

//         // Perform PATCH request
//         mockMvc.perform(patch("/api/projects/1/change-role")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"userId\": 2, \"role\": \"ADMIN\"}")
//                 .param("currentUserId", "1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.message").value("Rôle mis à jour avec succès."));
//     }

//     @Test
//     void testGetProjectsByUserId() throws Exception {
//         // Créer des objets ProjectRole pour les rôles des utilisateurs dans les projets
//         GetAllProjectsFromUserDTO.ProjectRole role1 = new GetAllProjectsFromUserDTO.ProjectRole();
//         role1.setUsername("John Doe");
//         role1.setRole("ADMIN");

//         GetAllProjectsFromUserDTO.ProjectRole role2 = new GetAllProjectsFromUserDTO.ProjectRole();
//         role2.setUsername("Jane Doe");
//         role2.setRole("MEMBER");

//         // Créer des objets GetAllProjectsFromUserDTO
//         GetAllProjectsFromUserDTO dto1 = new GetAllProjectsFromUserDTO();
//         dto1.setId(1L);
//         dto1.setName("Project 1");
//         dto1.setDescription("Description 1");
//         dto1.setCreatedAt(LocalDateTime.now());
//         dto1.setCreatedById(1L);
//         dto1.setProjectRoles(Arrays.asList(role1)); // Ajouter les rôles

//         GetAllProjectsFromUserDTO dto2 = new GetAllProjectsFromUserDTO();
//         dto2.setId(2L);
//         dto2.setName("Project 2");
//         dto2.setDescription("Description 2");
//         dto2.setCreatedAt(LocalDateTime.now());
//         dto2.setCreatedById(2L);
//         dto2.setProjectRoles(Arrays.asList(role2)); // Ajouter les rôles

//         // Mock service response
//         when(projectService.getProjectsByUserId(1L)).thenReturn(Arrays.asList(dto1, dto2));

//         // Perform GET request
//         mockMvc.perform(get("/api/projects/user/1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.size()").value(2))
//                 .andExpect(jsonPath("$[0].id").value(1))
//                 .andExpect(jsonPath("$[0].name").value("Project 1"))
//                 .andExpect(jsonPath("$[0].description").value("Description 1"))
//                 .andExpect(jsonPath("$[0].createdById").value(1))
//                 .andExpect(jsonPath("$[0].projectRoles[0].username").value("John Doe"))
//                 .andExpect(jsonPath("$[0].projectRoles[0].role").value("ADMIN"))
//                 .andExpect(jsonPath("$[1].id").value(2))
//                 .andExpect(jsonPath("$[1].name").value("Project 2"))
//                 .andExpect(jsonPath("$[1].description").value("Description 2"))
//                 .andExpect(jsonPath("$[1].createdById").value(2))
//                 .andExpect(jsonPath("$[1].projectRoles[0].username").value("Jane Doe"))
//                 .andExpect(jsonPath("$[1].projectRoles[0].role").value("MEMBER"));
//     }

//     @Test
//     void testInviteMemberToProject() throws Exception {
//         // Créer un utilisateur pour createdBy
//         Users createdByUser = new Users(1L, "John Doe", "password123", "john@example.com");

//         // Créer un objet Projects
//         LocalDateTime createdAt = LocalDateTime.now();
//         Projects project = new Projects("Project Name", createdByUser, "Description", createdAt);

//         // Créer un utilisateur invité
//         Users invitedUser = new Users(2L, "User", "password", "user@example.com");

//         // Mock service response
//         InviteMemberDTO request = new InviteMemberDTO("user@example.com", "MEMBER");
//         when(projectService.getProjectsById(1L)).thenReturn(Optional.of(project));
//         when(usersService.getUserByMail("user@example.com")).thenReturn(Optional.of(invitedUser));
//         doNothing().when(projectService).verifyAdminRole(1L, 1L);
//         doNothing().when(projectService).addUserToProject(project, invitedUser, "MEMBER");

//         // Perform POST request
//         mockMvc.perform(post("/api/projects/1/invite")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"email\": \"user@example.com\", \"role\": \"MEMBER\"}")
//                 .param("currentUserId", "1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name").value("Project Name"))
//                 .andExpect(jsonPath("$.description").value("Description"));
//     }
// }