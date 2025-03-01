package com.example.MPMT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.dto.TaskUpdateDTO;
import com.example.MPMT.model.*;
import com.example.MPMT.repository.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectsRepository projectsRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRoleRepository projectRoleRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskService taskService;

    private Users user;
    private Users newAssignee;
    private Projects project;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users(1L, "John Doe", "password123", "john@example.com");
        newAssignee = new Users(2L, "Jane Doe", "password123", "jane@example.com");
        project = new Projects("Test Project", user, "Description", LocalDateTime.now());
        task = new Task("Test Task", "Task Description", Task.Priority.HIGH, Task.Status.TODO, new Date(), project,
                user, user, new Date());
    }

    @Test
    void testGetTaskByIdFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.getTaskById(1L);

        assertTrue(foundTask.isPresent());
        assertEquals("Test Task", foundTask.get().getName());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Task> foundTask = taskService.getTaskById(1L);

        assertFalse(foundTask.isPresent());
    }

    @Test
    void testSaveTaskSuccess() {
        TaskCreationDTO dto = new TaskCreationDTO("Test Task", "Description", "HIGH", "TODO", new Date(), 1L, 1L, 1L);

        when(projectsRepository.findById(1L)).thenReturn(Optional.of(project));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRoleRepository.findByProjectAndUser(project, user))
                .thenReturn(new ProjectRole(project, user, ProjectRole.Role.ADMIN));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.saveTask(dto);

        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getName());

        // Utilisation correcte des matchers
        verify(emailService).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void testSaveTaskProjectNotFound() {
        TaskCreationDTO dto = new TaskCreationDTO("Test Task", "Description", "HIGH", "TODO", new Date(), 1L, 1L, 1L);

        when(projectsRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.saveTask(dto);
        });
        assertEquals("Projet non trouvé avec l'ID : 1", exception.getMessage());
    }

    @Test
    void testUpdateTaskSuccess() {
        TaskUpdateDTO dto = new TaskUpdateDTO("Updated Task", "Updated Description", "LOW", "DONE", new Date(), 2L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(usersRepository.findById(2L)).thenReturn(Optional.of(newAssignee));
        when(projectRoleRepository.findByProjectAndUser(project, user))
            .thenReturn(new ProjectRole(project, user, ProjectRole.Role.ADMIN));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(1L, dto, 1L);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getName());

        verify(emailService).sendEmail(eq(newAssignee.getEmail()), anyString(), anyString());
    }

    @Test
    void testDeleteTaskByIdSuccess() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L);

        verify(taskRepository).deleteById(1L);
    }
}