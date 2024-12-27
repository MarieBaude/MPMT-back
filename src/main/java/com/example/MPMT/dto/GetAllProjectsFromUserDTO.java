package com.example.MPMT.dto;
import java.util.List;

public class GetAllProjectsFromUserDTO {
    private Long id;
    private String name;
    private Long createdById;
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

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }

    public List<ProjectRole> getProjectRoles() { return projectRoles; }
    public void setProjectRoles(List<ProjectRole> projectRoles) { this.projectRoles = projectRoles; }

}
