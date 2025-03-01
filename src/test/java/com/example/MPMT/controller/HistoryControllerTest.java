package com.example.MPMT.controller;

import com.example.MPMT.dto.HistoryDTO;
import com.example.MPMT.model.History;
import com.example.MPMT.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
        mockMvc = MockMvcBuilders.standaloneSetup(historyController).build(); // Configuration de MockMvc
    }

    @Test
    void testGetTaskHistory() throws Exception {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setId(1L);
        historyDTO.setChangedField("status");
        historyDTO.setOldValue("TODO");
        historyDTO.setNewValue("IN_PROGRESS");

        List<HistoryDTO> histories = Collections.singletonList(historyDTO);

        when(historyService.getHistoriesByTaskId(anyLong())).thenReturn(histories);

        mockMvc.perform(get("/api/histories/task/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].changedField").value("status"))
                .andExpect(jsonPath("$[0].oldValue").value("TODO"))
                .andExpect(jsonPath("$[0].newValue").value("IN_PROGRESS"));
    }

    @Test
    void testSaveHistory() throws Exception {
        History history = new History();
        history.setId(1L);
        history.setChangedField("priority");
        history.setOldValue("LOW");
        history.setNewValue("HIGH");

        when(historyService.saveHistory(any(History.class))).thenReturn(history);

        mockMvc.perform(post("/api/histories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"changedField\":\"priority\",\"oldValue\":\"LOW\",\"newValue\":\"HIGH\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.changedField").value("priority"))
                .andExpect(jsonPath("$.oldValue").value("LOW"))
                .andExpect(jsonPath("$.newValue").value("HIGH"));
    }
}