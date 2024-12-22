package com.example.MPMT.dto;

public class InviteMemberDTO {
    private Long userId;
    private String role;

    public InviteMemberDTO() {}

    public InviteMemberDTO(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    // Getters et setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
