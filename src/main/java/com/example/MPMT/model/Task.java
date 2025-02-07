package com.example.MPMT.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects project;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    private Users assignee;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<History> updates;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Projects getProjects() {
        return project;
    }

    public void setProjects(Projects project) {
        this.project = project;
    }

    public Users getAssignee() {
        return assignee;
    }

    public void setAssignee(Users assignee) {
        this.assignee = assignee;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public List<History> getUpdates() {
        return updates;
    }

    public void setUpdates(List<History> updates) {
        this.updates = updates;
    }

    // Enums for Priority and Status

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE, CLOSED
    }
}