package com.example.MPMT.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.model.Users;
import com.example.MPMT.repository.ProjectsRepository;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.ProjectRole;
import com.example.MPMT.model.ProjectRole.Role;
import com.example.MPMT.repository.ProjectRoleRepository;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.dto.AssignRoleDTO;
import com.example.MPMT.dto.GetAllProjectsFromUserDTO;

@Service
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final UsersRepository usersRepository;
    private final ProjectRoleRepository projectRoleRepository;

    public ProjectsService(ProjectsRepository projectsRepository, UsersRepository usersRepository,
            ProjectRoleRepository projectRoleRepository) {
        this.projectsRepository = projectsRepository;
        this.usersRepository = usersRepository;
        this.projectRoleRepository = projectRoleRepository;
    }

    // Création d'un projet
    public Projects createProject(ProjectCreationDTO dto) {
        Users user = usersRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Projects project = new Projects(dto.getName(), user, dto.getDescription(), dto.getCreatedAt());
        Projects savedProject = projectsRepository.save(project);

        // Ajouter l'utilisateur en tant qu'ADMIN par défaut
        ProjectRole projectRole = new ProjectRole(savedProject, user, ProjectRole.Role.ADMIN);
        projectRoleRepository.save(projectRole);

        return savedProject;
    }

    // Trouver un projet
    public Optional<Projects> getProjectsById(Long id) {
        return projectsRepository.findById(id);
    }

    // Assigner un rôle à un utilisateur
    public void assignRole(AssignRoleDTO dto) {
        // Récupérer le projet
        Projects project = projectsRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Projet non trouvé avec l'ID : " + dto.getProjectId()));
    
        // Récupérer l'utilisateur
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + dto.getUserId()));
    
        // Vérifier que l'utilisateur fait déjà partie du projet
        ProjectRole existingRole = projectRoleRepository.findByProjectAndUser(project, user);
        if (existingRole == null) {
            throw new IllegalStateException("L'utilisateur n'est pas assigné au projet.");
        }
    
        // Valider le rôle et le mettre à jour
        try {
            ProjectRole.Role roleType = ProjectRole.Role.valueOf(dto.getRole().toUpperCase());
            existingRole.setRole(roleType);
            projectRoleRepository.save(existingRole);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle invalide : " + dto.getRole());
        }
    }    
    
    // Obtenir tous les projets d'un utilisateur
    public List<GetAllProjectsFromUserDTO> getProjectsByUserId(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Projects> projects = projectsRepository.findByCreatedByOrProjectRoles_User(user, user);

        // Convertir chaque projet en DTO
        return projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GetAllProjectsFromUserDTO convertToDto(Projects project) {
        GetAllProjectsFromUserDTO dto = new GetAllProjectsFromUserDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCreatedById(project.getCreatedBy().getId());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setDescription(project.getDescription());
        dto.setProjectRoles(project.getProjectRoles().stream()
                .map(role -> {
                    GetAllProjectsFromUserDTO.ProjectRole roleDto = new GetAllProjectsFromUserDTO.ProjectRole();
                    roleDto.setUsername(role.getUser().getUsername());
                    roleDto.setRole(role.getRole().name());
                    return roleDto;
                })
                .collect(Collectors.toList()));
        return dto;
    }

    // Inviter un utilisateur à un projet
    public void addUserToProject(Projects project, Users user, String role) {
        // Vérifier si le rôle est valide
        Role validatedRole = Role.valueOf(role.toUpperCase());

        // Vérifier si l'utilisateur est déjà membre du projet
        if (projectRoleRepository.existsByProjectAndUser(project, user)) {
            throw new IllegalArgumentException("Cet utilisateur est déjà membre du projet.");
        }

        // Créer et enregistrer le rôle
        ProjectRole projectRole = new ProjectRole();
        projectRole.setProject(project);
        projectRole.setUser(user);
        projectRole.setRole(validatedRole);

        projectRoleRepository.save(projectRole);
    }

    // Vérifier si l'utilisateur est un admin
    public void verifyAdminRole(Long projectId, Long userId) {
        List<ProjectRole> roles = projectRoleRepository.findByProjectIdAndUserId(projectId, userId);
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Accès refusé, vous n'êtes pas ADMIN");

        }

        boolean isAdmin = roles.stream()
                .anyMatch(projectRole -> projectRole.getRole() == Role.ADMIN);

        if (!isAdmin) {
            throw new IllegalArgumentException("Accès refusé, vous n'êtes pas ADMIN");
        }
    }

}
