package com.example.MPMT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.MPMT.repository.HistoryRepository;
import com.example.MPMT.repository.UsersRepository;
import com.example.MPMT.dto.HistoryDTO;
import com.example.MPMT.model.History;
import com.example.MPMT.model.Users;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    // Récupérer tous les historiques d'une tâche
    public List<HistoryDTO> getHistoriesByTaskId(Long taskId) {
        List<History> histories = historyRepository.findByTaskId(taskId);
        return histories.stream().map(history -> {
            HistoryDTO dto = new HistoryDTO();
            dto.setId(history.getId());
            dto.setChangedField(history.getChangedField());
            dto.setOldValue(history.getOldValue());
            dto.setNewValue(history.getNewValue());
            dto.setUpdatedAt(history.getUpdatedAt());
            dto.setUsername(history.getUsers().getUsername());

            // Si le champ modifié est l'assigné, récupérer les noms d'utilisateur
            if ("assignee".equals(history.getChangedField())) {
                Users oldAssignee = usersRepository.findById(Long.valueOf(history.getOldValue())).orElse(null);
                Users newAssignee = usersRepository.findById(Long.valueOf(history.getNewValue())).orElse(null);

                dto.setOldAssigneeUsername(oldAssignee != null ? oldAssignee.getUsername() : "Inconnu");
                dto.setNewAssigneeUsername(newAssignee != null ? newAssignee.getUsername() : "Inconnu");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    // Sauvegarder un historique
    public History saveHistory(History history) {
        return historyRepository.save(history);
    }

}