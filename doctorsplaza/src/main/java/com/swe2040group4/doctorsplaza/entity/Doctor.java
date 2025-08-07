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
@Table (name = "doctor")

public class Doctor {
    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "doctor_id")
    private Long doctorId;
    
   @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
   
   @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
   @Column(name = "email", nullable = false, length = 100)
    private String email;
    
   @Column(name = "password_hash", nullable = false, length = 100)
    private String password;
    
   @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    
   @Column(name = "office_floor", nullable = false)
    private int officeFloor;
    
   @Column(name = "office_room", nullable = false)
    private int officeRoom;
    
   @Column(name = "specialization", length = 100)
    private String specialization;
   
   @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id", nullable = false)
    private Admin createdByAdmin;
    
    public Doctor() {}
    
    public Long getDoctorId(){
        return doctorId;
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
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public int getOfficeFloor(){
        return officeFloor;
    }
    
    public int getOfficeRoom(){
        return officeRoom;
    }
    
    public String getSpecialization(){
        return specialization;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public Admin getCreatedByAdmin() {
        return createdByAdmin;
    }
    
    public void setDoctorId(Long doctorId){
        this.doctorId = doctorId;
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
    
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    public void setOfficeFloor(int officeFloor){
        this.officeFloor = officeFloor;
    }
    
    public void setOfficeRoom(int officeRoom){
        this.officeRoom = officeRoom;
    }
    
    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }
    
    public void setCreatedByAdmin(Admin createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    @Override
    public String toString() {
        return "Doctor{" + "doctorId=" + doctorId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password + ", phoneNumber=" + phoneNumber + ", officeFloor=" + officeFloor + ", officeRoom=" + officeRoom + ", specialization=" + specialization + ", createdAt=" + createdAt + ", createdByAdmin=" + createdByAdmin + '}';
    }
    
}
    
