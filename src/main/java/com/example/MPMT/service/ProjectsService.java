package com.example.MPMT.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.model.Users;
import com.example.MPMT.repository.ProjectsRepository;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.ProjectRole;
import com.example.MPMT.repository.ProjectRoleRepository;
import com.example.MPMT.dto.ProjectCreationDTO;
import com.example.MPMT.dto.AssignRoleDTO;

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

        Projects project = new Projects(dto.getName(), user);
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
        Projects project = projectsRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ProjectRole.Role roleType = ProjectRole.Role.valueOf(dto.getRole().toUpperCase());

        ProjectRole projectRole = new ProjectRole(project, user, roleType);
        projectRoleRepository.save(projectRole);
    }
}
