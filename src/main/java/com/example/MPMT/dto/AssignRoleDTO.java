package com.example.MPMT.dto;

public class AssignRoleDTO {
    private Long projectId;
    private Long userId;
    private String role;

    // Getters et Setters
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

