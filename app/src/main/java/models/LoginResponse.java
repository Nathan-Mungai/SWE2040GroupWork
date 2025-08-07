package models;

public class LoginResponse {
    private Long userId;
    private String role;

    // Constructors, getters, and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}