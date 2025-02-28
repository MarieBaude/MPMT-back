package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.MPMT.repository.HistoryRepository;
import com.example.MPMT.repository.ProjectRoleRepository;
import com.example.MPMT.repository.ProjectsRepository;
import com.example.MPMT.repository.TaskRepository;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.dto.TaskUpdateDTO;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Task;
import com.example.MPMT.model.Users;
import com.example.MPMT.model.History;
import com.example.MPMT.model.ProjectRole;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private EmailService emailService;

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
        Projects project = projectsRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID : " + taskDTO.getProjectId()));

        Users assignee = null;
        if (taskDTO.getAssigneeId() != null) {
            assignee = usersRepository.findById(taskDTO.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Utilisateur assigné non trouvé avec l'ID : " + taskDTO.getAssigneeId()));
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

    // Mettre à jour une tâche et créer un historique
    public Task updateTask(Long taskId, TaskUpdateDTO taskDTO, Long userId) {
        // Récupérer la tâche existante
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec l'ID : " + taskId));
    
        // Récupérer l'utilisateur qui effectue la modification
        Users currentUser = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId));
    
        // Vérifier le rôle de l'utilisateur dans le projet
        ProjectRole projectRole = projectRoleRepository.findByProjectAndUser(task.getProjects(), currentUser);
        if (projectRole == null || projectRole.getRole() == ProjectRole.Role.OBSERVER) {
            throw new RuntimeException("L'utilisateur n'a pas les permissions nécessaires pour modifier la tâche.");
        }
    
        // Enregistrer les anciennes valeurs
        Map<String, String> changes = new HashMap<>();
    
        // Mettre à jour uniquement les champs fournis
        if (taskDTO.getName() != null && !taskDTO.getName().equals(task.getName())) {
            changes.put("name", task.getName()); // Ancienne valeur
            task.setName(taskDTO.getName()); // Nouvelle valeur
        }
    
        if (taskDTO.getDescription() != null && !taskDTO.getDescription().equals(task.getDescription())) {
            changes.put("description", task.getDescription());
            task.setDescription(taskDTO.getDescription());
        }
    
        if (taskDTO.getPriority() != null && !taskDTO.getPriority().equals(task.getPriority().name())) {
            changes.put("priority", task.getPriority().name());
            task.setPriority(Task.Priority.valueOf(taskDTO.getPriority()));
        }
    
        if (taskDTO.getStatus() != null && !taskDTO.getStatus().equals(task.getStatus().name())) {
            changes.put("status", task.getStatus().name());
            task.setStatus(Task.Status.valueOf(taskDTO.getStatus()));
        }
    
        if (taskDTO.getEndDate() != null && !taskDTO.getEndDate().equals(task.getEndDate())) {
            changes.put("endDate", task.getEndDate().toString());
            task.setEndDate(taskDTO.getEndDate());
        }
    
        // Vérifier si l'assigné a changé
        Users newAssignee = null;
        if (taskDTO.getAssigneeId() != null) {
            newAssignee = usersRepository.findById(taskDTO.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur assigné non trouvé avec l'ID : " + taskDTO.getAssigneeId()));
    
            if (!newAssignee.equals(task.getAssignee())) {
                changes.put("assignee", task.getAssignee() != null ? task.getAssignee().getId().toString() : "null");
                task.setAssignee(newAssignee);
                String subject = "Vous avez été assigné à une nouvelle tâche";
                String text = "Vous avez été assigné à la tâche : " + task.getName() + ", dans le projet : " + task.getProjects().getName();
                emailService.sendEmail(newAssignee.getEmail(), subject, text);
            }
        }
    
        // Sauvegarder la tâche mise à jour
        Task updatedTask = taskRepository.save(task);
    
        // Enregistrer les changements dans l'historique
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            History history = new History();
            history.setChangedField(entry.getKey());
            history.setOldValue(entry.getValue());
            history.setNewValue(getNewValue(entry.getKey(), task)); // Méthode pour récupérer la nouvelle valeur
            history.setUpdatedAt(LocalDateTime.now());
            history.setTask(updatedTask);
            history.setUsers(currentUser); // Utilisateur qui a effectué la modification
    
            historyRepository.save(history);
        }
    
        // Envoyer un e-mail si l'assigné a changé
        if (newAssignee != null && !newAssignee.equals(task.getAssignee())) {
            String subject = "Vous avez été assigné à une nouvelle tâche";
            String text = "Vous avez été assigné à la tâche : " + task.getName() + " dans le projet : " + task.getProjects().getName();
            emailService.sendEmail(newAssignee.getEmail(), subject, text);
        }
    
        return updatedTask;
    }

    // Méthode pour récupérer la nouvelle valeur d'un champ
    private String getNewValue(String field, Task task) {
        switch (field) {
            case "name":
                return task.getName();
            case "description":
                return task.getDescription();
            case "priority":
                return task.getPriority().name();
            case "status":
                return task.getStatus().name();
            case "endDate":
                return task.getEndDate().toString();
            case "assignee":
                return task.getAssignee() != null ? task.getAssignee().getId().toString() : "null";
            default:
                return "";
        }
    }

    // Supprimer
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}