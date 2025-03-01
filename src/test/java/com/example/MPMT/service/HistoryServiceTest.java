package com.example.MPMT.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.MPMT.dto.HistoryDTO;
import com.example.MPMT.model.History;
import com.example.MPMT.model.Users;
import com.example.MPMT.repository.HistoryRepository;
import com.example.MPMT.repository.UsersRepository;

public class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private HistoryService historyService;

    private Users user1;
    private Users user2;
    private History history1;
    private History history2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks

        user1 = new Users(1L, "John Doe", "password123", "john@example.com");
        user2 = new Users(2L, "Jane Doe", "password456", "jane@example.com");

        history1 = new History();
        history1.setId(1L);
        history1.setChangedField("name");
        history1.setOldValue("Old Name");
        history1.setNewValue("New Name");
        history1.setUpdatedAt(LocalDateTime.now());
        history1.setUsers(user1);

        history2 = new History();
        history2.setId(2L);
        history2.setChangedField("assignee");
        history2.setOldValue("1"); // ID de l'ancien assigné
        history2.setNewValue("2"); // ID du nouvel assigné
        history2.setUpdatedAt(LocalDateTime.now());
        history2.setUsers(user1);
    }

    @Test
    void testGetHistoriesByTaskIdSuccess() {
        when(historyRepository.findByTaskId(1L)).thenReturn(Arrays.asList(history1, history2));
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(usersRepository.findById(2L)).thenReturn(Optional.of(user2));

        List<HistoryDTO> histories = historyService.getHistoriesByTaskId(1L);

        // Assert
        assertNotNull(histories);
        assertEquals(2, histories.size());

        // Vérifier le premier historique
        HistoryDTO dto1 = histories.get(0);
        assertEquals("name", dto1.getChangedField());
        assertEquals("Old Name", dto1.getOldValue());
        assertEquals("New Name", dto1.getNewValue());
        assertEquals(user1.getUsername(), dto1.getUsername());

        // Vérifier le deuxième historique
        HistoryDTO dto2 = histories.get(1);
        assertEquals("assignee", dto2.getChangedField());
        assertEquals(user1.getUsername(), dto2.getOldAssigneeUsername());
        assertEquals(user2.getUsername(), dto2.getNewAssigneeUsername());
    }

    @Test
    void testGetHistoriesByTaskIdAssigneeNotFound() {
        when(historyRepository.findByTaskId(1L)).thenReturn(Arrays.asList(history2));
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());
        when(usersRepository.findById(2L)).thenReturn(Optional.empty());

        List<HistoryDTO> histories = historyService.getHistoriesByTaskId(1L);

        assertNotNull(histories);
        assertEquals(1, histories.size());

        HistoryDTO dto = histories.get(0);
        assertEquals("assignee", dto.getChangedField());
        assertEquals("Inconnu", dto.getOldAssigneeUsername());
        assertEquals("Inconnu", dto.getNewAssigneeUsername());
    }

    @Test
    void testGetHistoriesByTaskIdNoHistories() {
        when(historyRepository.findByTaskId(1L)).thenReturn(Arrays.asList());

        List<HistoryDTO> histories = historyService.getHistoriesByTaskId(1L);

        assertNotNull(histories);
        assertTrue(histories.isEmpty());
    }

    @Test
    void testSaveHistorySuccess() {
        when(historyRepository.save(any(History.class))).thenReturn(history1);

        History savedHistory = historyService.saveHistory(history1);

        assertNotNull(savedHistory);
        assertEquals(1L, savedHistory.getId());
        assertEquals("name", savedHistory.getChangedField());
        assertEquals("Old Name", savedHistory.getOldValue());
        assertEquals("New Name", savedHistory.getNewValue());
        assertEquals(user1, savedHistory.getUsers());

        verify(historyRepository).save(history1);
    }
}