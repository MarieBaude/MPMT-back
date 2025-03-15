package com.example.MPMT.controller;

import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Users;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.dto.AssignRoleDTO;
import com.example.MPMT.dto.GetAllProjectsFromUserDTO;
import com.example.MPMT.dto.InviteMemberDTO;
import com.example.MPMT.service.ProjectsService;
import com.example.MPMT.service.UsersService;

@RestController
@CrossOrigin(origins = "http://localhost:7085")
@RequestMapping("/api/projects")
public class ProjectsController {

    private final ProjectsService projectService;
    private final UsersService userService;

    public ProjectsController(ProjectsService projectService, UsersService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    // Trouver tous les projets
    @GetMapping
    public List<Projects> getAllUsers() {
        return projectService.findAll();
    }

    // Créer un projet
    @PostMapping("/create")
    public ResponseEntity<Projects> createProject(@RequestBody ProjectCreationDTO dto) {
        Projects project = projectService.createProject(dto);
        return ResponseEntity.ok(project);
    }

    // Récupérer un projet par son id
    @GetMapping("/{id}")
    public Optional<Projects> projectService(@PathVariable Long id) {
        return projectService.getProjectsById(id);
    }

    // Changer le rôle d'utilisateur dans un projet
    @PatchMapping("/{projectId}/change-role")
    public ResponseEntity<Map<String, String>> changeUserRole(
            @PathVariable Long projectId,
            @RequestBody AssignRoleDTO dto,
            @RequestParam Long currentUserId) {

        // Vérifier si l'utilisateur a le rôle d'admin dans le projet
        projectService.verifyAdminRole(projectId, currentUserId);

        // Assigner le rôle à l'utilisateur
        dto.setProjectId(projectId); // S'assurer que l'ID du projet est bien dans le DTO
        projectService.assignRole(dto);

        // Créer une réponse JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Rôle mis à jour avec succès.");

        return ResponseEntity.ok(response);
    }

    // Récupérer tous les projets d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetAllProjectsFromUserDTO>> getProjectsByUserId(@PathVariable Long userId) {
        List<GetAllProjectsFromUserDTO> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    // Inviter un utilisateur à un projet
    @PostMapping("/{projectId}/invite")
    public ResponseEntity<?> inviteMemberToProject(
            @PathVariable Long projectId,
            @RequestBody InviteMemberDTO request,
            @RequestParam Long currentUserId) {

        // Vérifier si l'utilisateur a le rôle d'admin dans le projet
        projectService.verifyAdminRole(projectId, currentUserId);

        // Vérifier si le projet existe
        Projects project = projectService.getProjectsById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Projet introuvable"));

        // Vérifier si l'utilisateur à inviter existe en utilisant l'email
        Users invitedUser = userService.getUserByMail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));

        // Ajouter l'utilisateur au projet avec le rôle spécifié
        projectService.addUserToProject(project, invitedUser, request.getRole());

        return ResponseEntity.ok(project);
    }

    // Récupérer les membres d'un projet
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<Users>> getProjectMembers(@PathVariable Long projectId) {
        List<Users> members = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(members);
    }

}
