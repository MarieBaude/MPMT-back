package com.example.MPMT.controller;

import com.example.MPMT.dto.TaskCreationDTO;
import com.example.MPMT.dto.TaskUpdateDTO;
import com.example.MPMT.model.Task;
import com.example.MPMT.model.Users;
import com.example.MPMT.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build(); // Configuration de MockMvc
    }

    @Test
    void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    void testGetTasksByProjectId() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setName("Test Task");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Task.Status.TODO);
        task.setEndDate(new Date());
        task.setCreatedAt(new Date());

        Users assignee = new Users();
        assignee.setId(1L);
        assignee.setUsername("testuser");
        task.setAssignee(assignee);

        List<Task> tasks = Collections.singletonList(task);

        when(taskService.getTasksByProjectId(1L)).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/project/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Task"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[0].assignee.id").value(1L))
                .andExpect(jsonPath("$[0].assignee.username").value("testuser"));
    }

    @Test
    void testSaveTask() throws Exception {
        TaskCreationDTO taskDTO = new TaskCreationDTO();
        taskDTO.setName("New Task");
        taskDTO.setDescription("Task Description");
        taskDTO.setPriority("HIGH");
        taskDTO.setStatus("TODO");
        taskDTO.setEndDate(new Date());
        taskDTO.setProjectId(1L);
        taskDTO.setAssigneeId(1L);
        taskDTO.setCreatedById(1L);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setName("New Task");

        when(taskService.saveTask(any(TaskCreationDTO.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Task\",\"description\":\"Task Description\",\"priority\":\"HIGH\",\"status\":\"TODO\",\"endDate\":\"2023-10-10T12:34:56.789\",\"projectId\":1,\"assigneeId\":1,\"createdById\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskUpdateDTO taskDTO = new TaskUpdateDTO();
        taskDTO.setName("Updated Task");
        taskDTO.setDescription("Updated Description");
        taskDTO.setPriority("LOW");
        taskDTO.setStatus("DONE");
        taskDTO.setEndDate(new Date());
        taskDTO.setAssigneeId(2L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setName("Updated Task");

        when(taskService.updateTask(anyLong(), any(TaskUpdateDTO.class), anyLong())).thenReturn(updatedTask);

        mockMvc.perform(patch("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Task\",\"description\":\"Updated Description\",\"priority\":\"LOW\",\"status\":\"DONE\",\"endDate\":\"2023-10-10T12:34:56.789\",\"assigneeId\":2}")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Task"));
    }

    @Test
    void testDeleteTaskById() throws Exception {
        doNothing().when(taskService).deleteTaskById(1L);

        mockMvc.perform(delete("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTaskById(1L);
    }
}