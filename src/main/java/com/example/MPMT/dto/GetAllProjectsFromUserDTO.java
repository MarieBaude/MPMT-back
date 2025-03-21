package com.example.MPMT.dto;
import java.time.LocalDateTime;
import java.util.List;

public class GetAllProjectsFromUserDTO {
    private Long id;
    private String name;
    private Long createdById;
    private LocalDateTime createdAt;
    private String description;
    private List<ProjectRole> projectRoles;
    
    public static class ProjectRole {
        private String username;
        private String role;

        // Getters et Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ProjectRole> getProjectRoles() { return projectRoles; }
    public void setProjectRoles(List<ProjectRole> projectRoles) { this.projectRoles = projectRoles; }

}
