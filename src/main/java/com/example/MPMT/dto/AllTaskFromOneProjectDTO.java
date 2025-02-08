package com.example.MPMT.dto;


import java.util.Date;

public class AllTaskFromOneProjectDTO {

    private Long id;
    private String name;
    private String priority;
    private String status;
    private Date endDate;
    private Date createdAt;
    private AssigneeDTO assignee;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public AssigneeDTO getAssignee() {
        return assignee;
    }

    public void setAssignee(AssigneeDTO assignee) {
        this.assignee = assignee;
    }

    // Classe interne pour Assignee
    public static class AssigneeDTO {
        private Long id;
        private String username;

        // Getters and Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}