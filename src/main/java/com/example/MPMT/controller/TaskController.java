package com.example.MPMT.controller;

import com.example.MPMT.dto.AllTaskFromOneProjectDTO;
import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.dto.TaskUpdateDTO;
import com.example.MPMT.model.Task;
import com.example.MPMT.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Récupérer une tâche par son ID
    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // Récupérer toutes les tâches d'un projet
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<AllTaskFromOneProjectDTO>> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);

        // Convertir les tâches en DTO
        List<AllTaskFromOneProjectDTO> taskDTOs = tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskDTOs);
    }

    // Méthode pour convertir une Task en AllTaskFromOneProjectDTO
    private AllTaskFromOneProjectDTO convertToDTO(Task task) {
        AllTaskFromOneProjectDTO dto = new AllTaskFromOneProjectDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setPriority(task.getPriority().name()); // Convertir l'enum en String
        dto.setStatus(task.getStatus().name()); // Convertir l'enum en String
        dto.setEndDate(task.getEndDate());
        dto.setCreatedAt(task.getCreatedAt());

        // Convertir l'assignee en DTO
        AllTaskFromOneProjectDTO.AssigneeDTO assigneeDTO = new AllTaskFromOneProjectDTO.AssigneeDTO();
        assigneeDTO.setId(task.getAssignee().getId());
        assigneeDTO.setUsername(task.getAssignee().getUsername());
        dto.setAssignee(assigneeDTO);

        return dto;
    }

    // Créer une tâche
    @PostMapping
    public ResponseEntity<?> saveTask(@RequestBody TaskCreationDTO taskDTO) {
        try {
            Task savedTask = taskService.saveTask(taskDTO);
            return ResponseEntity.ok(savedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Mettre à jour une tâche et créer un historique
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody TaskUpdateDTO taskDTO,
            @RequestParam Long userId) {
        Task updatedTask = taskService.updateTask(id, taskDTO, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Supprimer une tâche par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}