package com.example.MPMT.dto;

import java.time.LocalDateTime;

public class ProjectCreationDTO {
    private String name;
    private Long createdById;
    private String description;
    private LocalDateTime createdAt;

    public ProjectCreationDTO(String name, String description, LocalDateTime createdAt, Long createdById) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.createdById = createdById;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}