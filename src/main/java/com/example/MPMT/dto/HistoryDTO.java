package com.example.MPMT.dto;

import java.time.LocalDateTime;

public class HistoryDTO {
    private Long id;
    private String changedField;
    private String oldValue;
    private String newValue;
    private LocalDateTime updatedAt;
    private String username; 
    private String oldAssigneeUsername; 
    private String newAssigneeUsername; 

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChangedField() {
        return changedField;
    }

    public void setChangedField(String changedField) {
        this.changedField = changedField;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldAssigneeUsername() {
        return oldAssigneeUsername;
    }

    public void setOldAssigneeUsername(String oldAssigneeUsername) {
        this.oldAssigneeUsername = oldAssigneeUsername;
    }

    public String getNewAssigneeUsername() {
        return newAssigneeUsername;
    }

    public void setNewAssigneeUsername(String newAssigneeUsername) {
        this.newAssigneeUsername = newAssigneeUsername;
    }
}
