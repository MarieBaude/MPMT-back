package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MPMT.repository.ProjectRoleRepository;
import com.example.MPMT.repository.ProjectsRepository;
import com.example.MPMT.repository.TaskRepository;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Task;
import com.example.MPMT.model.Users;
import com.example.MPMT.model.ProjectRole;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRoleRepository projectRoleRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Récupérer une tâche par son ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Récupérer toutes les tâches d'un projet
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // Créer une tâche
    public Task saveTask(TaskCreationDTO taskDTO) {
        // Charger les entités associées à partir des IDs
        Projects project = projectsRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID : " + taskDTO.getProjectId()));

        Users assignee = null;
        if (taskDTO.getAssigneeId() != null) {
            assignee = usersRepository.findById(taskDTO.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur assigné non trouvé avec l'ID : " + taskDTO.getAssigneeId()));
        }

        Users createdBy = usersRepository.findById(taskDTO.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Créateur non trouvé avec l'ID : " + taskDTO.getCreatedById()));

        // Vérifier le rôle de l'utilisateur dans le projet
        ProjectRole projectRole = projectRoleRepository.findByProjectAndUser(project, createdBy);
        if (projectRole == null || projectRole.getRole() == ProjectRole.Role.OBSERVER) {
            throw new RuntimeException("L'utilisateur n'a pas les permissions nécessaires pour créer une tâche.");
        }

        // Créer une nouvelle tâche
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(Task.Priority.valueOf(taskDTO.getPriority()));
        task.setStatus(Task.Status.valueOf(taskDTO.getStatus()));
        task.setEndDate(taskDTO.getEndDate());
        task.setCreatedAt(new Date());
        task.setProjects(project);
        task.setAssignee(assignee);
        task.setCreatedBy(createdBy);

        // Sauvegarder la tâche
        return taskRepository.save(task);
    }

    // public Task saveTask(TaskCreationDTO taskDTO) {
    //     // Charger les entités associées à partir des IDs
    //     Projects project = projectsRepository.findById(taskDTO.getProjectId())
    //             .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID : " + taskDTO.getProjectId()));

    //     Users assignee = null;
    //     if (taskDTO.getAssigneeId() != null) {
    //         assignee = usersRepository.findById(taskDTO.getAssigneeId())
    //                 .orElseThrow(() -> new RuntimeException(
    //                         "Utilisateur assigné non trouvé avec l'ID : " + taskDTO.getAssigneeId()));
    //     }

    //     Users createdBy = usersRepository.findById(taskDTO.getCreatedById())
    //             .orElseThrow(() -> new RuntimeException("Créateur non trouvé avec l'ID : " + taskDTO.getCreatedById()));

    //     // Créer une nouvelle tâche
    //     Task task = new Task();
    //     task.setName(taskDTO.getName());
    //     task.setDescription(taskDTO.getDescription());
    //     task.setPriority(Task.Priority.valueOf(taskDTO.getPriority()));
    //     task.setStatus(Task.Status.valueOf(taskDTO.getStatus()));
    //     task.setEndDate(taskDTO.getEndDate());
    //     task.setCreatedAt(new Date());
    //     task.setProjects(project);
    //     task.setAssignee(assignee);
    //     task.setCreatedBy(createdBy);

    //     // Sauvegarder la tâche
    //     return taskRepository.save(task);
    // }

    // // Mettre à jour une tâche et créer un historique

    // Supprimer
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}