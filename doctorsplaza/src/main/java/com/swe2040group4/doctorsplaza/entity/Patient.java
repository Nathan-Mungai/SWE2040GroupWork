/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 *
 * @author akuecyel
 */
@Entity
@Table(name = "patient")

public class Patient {
    
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }
    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    
   @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
   
   @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
   
   @Enumerated(EnumType.STRING)
   @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;
    
   @Column(name ="date_of_birth", nullable = false)
   private LocalDate dateOfBirth;
   
   @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
   
   @Column(name = "email", nullable = false, length = 100)
    private String email;
    
   @Column(name = "id_number", nullable = false, length = 50)
    private String idNumber;
    
   @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    
   @Column(name = "emergency_contact_number", length = 20)
    private String emergencyContactNumber;
   
   @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;
    
   @Column(name = "insurance_number", length = 100)
    private String insuranceNumber;
   
   @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
    
    public Patient() {}
    
    public Long getPatientId(){
        return patientId;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public Gender getGender(){
        return gender;
    }
    
    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getEmail(){
        return email;
    }
    
    public String getIdNumber(){
        return idNumber;
    }
    
    public String getEmergencyContactName(){
        return emergencyContactName;
    }
    
    public String getEmergencyContactNumber(){
        return emergencyContactNumber;
    }   
    
    public String getInsuranceProvider(){
        return insuranceProvider;
    }
    
    public String getInsuranceNumber(){
        return insuranceNumber;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public void setPatientId(Long patientId){
        this.patientId = patientId;
    }
    
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public void setGender(Gender gender){
        this.gender = gender;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setIdNumber(String idNumber){
        this.idNumber = idNumber;
    }
    
    public void setEmergencyContactName(String emergencyContactName){
        this.emergencyContactName = emergencyContactName;
    }
    
    public void setEmergencyContactNumber(String emergencyContactNumber){
        this.emergencyContactNumber = emergencyContactNumber;
    }
    
    public void setInsuranceProvider(String insuranceProvider){
        this.insuranceProvider = insuranceProvider;
    }
    
    public void setInsuranceNumber(String insuranceNumber){
        this.insuranceNumber = insuranceNumber;
    }

    @Override
    public String toString() {
        return "Patient{" + "patientId=" + patientId + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", phoneNumber=" + phoneNumber + ", email=" + email + ", idNumber=" + idNumber + ", emergencyContactName=" + emergencyContactName + ", emergencyContactNumber=" + emergencyContactNumber + ", insuranceProvider=" + insuranceProvider + ", insuranceNumber=" + insuranceNumber + ", createdAt=" + createdAt + '}';
    }
    
}
    

