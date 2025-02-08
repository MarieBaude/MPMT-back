package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MPMT.repository.HistoryRepository;
import com.example.MPMT.model.History;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // Récupérer tous les historiques d'une tâche
    public List<History> getHistoriesByTaskId(Long taskId) {
        return historyRepository.findByTaskId(taskId);
    }

    // Sauvegarder un historique
    public History saveHistory(History history) {
        return historyRepository.save(history);
    }

}