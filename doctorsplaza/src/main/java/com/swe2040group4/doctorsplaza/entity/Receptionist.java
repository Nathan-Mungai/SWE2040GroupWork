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
@Table(name = "receptionist")
public class Receptionist {
    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receptionistId;
    
   @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
   
   @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
   @Column(name = "email", nullable = false, length = 100)
    private String email;
    
   @Column(name = "password_hash", nullable = false, length = 100)
    private String password;

   @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctorId;
   
   @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

   @ManyToOne
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private Admin createdByAdmin;
    
    public Receptionist() {}
    
    public Long getReceptionistId(){
        return receptionistId;
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
    
    public Doctor getDoctorId() { 
        return doctorId; 
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public Admin getCreatedByAdmin() {
        return createdByAdmin;
    }
    
    public void setReceptionistId(Long receptionistId){
        this.receptionistId = receptionistId;
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
    
    public void setDoctorId(Doctor doctorId) { 
        this.doctorId = doctorId; 
    
    }
    public void setCreatedByAdmin(Admin createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    @Override
    public String toString() {
        return "Receptionist{" + "receptionistId=" + receptionistId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ", doctorId=" + doctorId + ", createdAt=" + createdAt + ", createdByAdmin=" + createdByAdmin + '}';
    }
    
}
    