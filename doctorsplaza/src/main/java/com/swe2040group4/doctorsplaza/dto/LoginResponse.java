package com.swe2040group4.doctorsplaza.dto;

public class LoginResponse {
    private Long userId;
    private String email;
    private String role;
    private Long assignedDoctorId;

    public LoginResponse(Long userId, String email, String role, Long assignedDoctorId) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.assignedDoctorId = assignedDoctorId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getAssignedDoctorId() {
        return assignedDoctorId;
    }
}