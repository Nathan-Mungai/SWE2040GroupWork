/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author akuecyel
 */
@Entity
@Table(name = "admin")
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AdminId;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "password_hash", nullable = false, length = 100)
    private String password;
    
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
    
    public Admin(){}
    
    public Long getAdminID(){
        return AdminId;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public void setAdminId(Long AdminID){
        this.AdminId = AdminId;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" + "AdminId=" + AdminId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ", createdAt=" + createdAt + '}';
    }

}
