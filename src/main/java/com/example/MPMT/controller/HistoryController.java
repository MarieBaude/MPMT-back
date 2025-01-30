package com.example.MPMT.controller;

import com.example.MPMT.model.History;
import com.example.MPMT.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/histories")
public class HistoryController {

    @Autowired
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    // Récupérer tous les historiques d'une tâche
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<History>> getHistoriesByTaskId(@PathVariable Long taskId) {
        List<History> histories = historyService.getHistoriesByTaskId(taskId);
        return ResponseEntity.ok(histories);
    }

    // Créer un historique
    @PostMapping
    public ResponseEntity<History> saveHistory(@RequestBody History history) {
        History savedHistory = historyService.saveHistory(history);
        return ResponseEntity.ok(savedHistory);
    }
}