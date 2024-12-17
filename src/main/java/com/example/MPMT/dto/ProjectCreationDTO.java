package com.example.MPMT.dto;

public class ProjectCreationDTO {
    private String name;
    private Long createdById;

    // Getters et Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}

