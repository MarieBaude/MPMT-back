package com.example.MPMT.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.MPMT.controller.TaskController;
import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.model.Projects;
import com.example.MPMT.model.Task;
import com.example.MPMT.model.Users;
import com.example.MPMT.service.DataGeneratorService;
import com.example.MPMT.service.TaskService;

@WebMvcTest(TaskController.class) 
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean
    private TaskService taskService; 

    @MockBean
    private DataGeneratorService dataGeneratorService;

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void testGetTaskById() throws Exception {
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        Projects project = new Projects();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Description");
        project.setCreatedBy(user);
        project.setCreatedAt(LocalDateTime.now()); 

        Task task = new Task();
        task.setId(1L);
        task.setName("Task 1");
        task.setDescription("Description");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Task.Status.TODO);
        task.setEndDate(toDate(LocalDateTime.now())); 
        task.setCreatedAt(toDate(LocalDateTime.now())); 
        task.setProjects(project);
        task.setAssignee(user);
        task.setCreatedBy(user);

        when(taskService.getTaskById(anyLong())).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id").value(1L)) 
                .andExpect(jsonPath("$.name").value("Task 1")) 
                .andExpect(jsonPath("$.priority").value("HIGH")) 
                .andExpect(jsonPath("$.status").value("TODO")); 
    }

    @Test
    void testSaveTask() throws Exception {
        // Données d'entrée
        TaskCreationDTO taskDTO = new TaskCreationDTO();
        taskDTO.setName("New Task");
        taskDTO.setDescription("Description");
        taskDTO.setPriority("HIGH");
        taskDTO.setStatus("TODO");
        taskDTO.setEndDate(toDate(LocalDateTime.now())); 
        taskDTO.setProjectId(1L);
        taskDTO.setCreatedById(1L);

        // Simuler la réponse du service
        Users user = new Users(1L, "John Doe", "password123", "john@example.com");
        Projects project = new Projects();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Description");
        project.setCreatedBy(user);
        project.setCreatedAt(LocalDateTime.now()); 

        Task task = new Task();
        task.setId(1L);
        task.setName("New Task");
        task.setDescription("Description");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Task.Status.TODO);
        task.setEndDate(toDate(LocalDateTime.now()));
        task.setCreatedAt(toDate(LocalDateTime.now())); 
        task.setProjects(project);
        task.setAssignee(user);
        task.setCreatedBy(user);

        when(taskService.saveTask(any(TaskCreationDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"New Task\",\"description\":\"Description\",\"priority\":\"HIGH\",\"status\":\"TODO\",\"endDate\":\"2023-10-01T12:00:00\",\"projectId\":1,\"createdById\":1}"))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.name").value("New Task")); 
    }
}