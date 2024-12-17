package com.example.MPMT.model;

import jakarta.persistence.*;

@Entity
@Table(name = "project_roles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    // Enum pour les rôles
    public enum RoleType {
        ADMIN,
        MEMBER,
        OBSERVER
    }

    // Constructeurs
    public ProjectRole() {}

    public ProjectRole(Projects project, Users user, RoleType role) {
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

    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }
}
