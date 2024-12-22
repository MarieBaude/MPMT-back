package com.example.MPMT.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "project_roles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Projects project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Enum pour les rôles
    public enum Role {
        ADMIN,
        MEMBER,
        OBSERVER
    }

     // Propriétés calculées pour le JSON
    @JsonProperty("role")
    public String getRoleName() {
        return role.name();
    }

    // Constructeurs
    public ProjectRole() {}

    public ProjectRole(Projects project, Users user, Role role) {
        this.project = project;
        this.user = user;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Projects getProject() { return project; }
    public void setProject(Projects project) { this.project = project; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
