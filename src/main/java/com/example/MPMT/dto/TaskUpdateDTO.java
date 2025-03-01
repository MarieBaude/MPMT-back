package com.example.MPMT.dto;

import java.util.Date;

public class TaskUpdateDTO {
    private String name;
    private String description;
    private String priority;
    private String status;
    private Date endDate;
    private Long assigneeId;

    public TaskUpdateDTO() {
    }

    public TaskUpdateDTO(String name, String description, String priority, String status, Date endDate, Long assigneeId) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.endDate = endDate;
        this.assigneeId = assigneeId;
    }

    // Getters et Setters
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

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

}
