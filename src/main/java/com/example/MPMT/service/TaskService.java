package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MPMT.repository.TaskRepository;
import com.example.MPMT.model.Task;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

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
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // Mettre à jour une tâche et créer un historique
    

    // Supprimer 
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}