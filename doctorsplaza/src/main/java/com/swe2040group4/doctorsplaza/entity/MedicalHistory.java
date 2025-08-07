/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.entity;

import jakarta.persistence.*;

/**
 *
 * @author akuecyel
 */
@Entity
@Table(name = "medical_history")

public class MedicalHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patientId;
    
    @Column(name = "family_history", nullable = false)
    private String familyHistory;
    
    @Column(name = "pre_existing_conditions", nullable = false)
    private String preExistingConditions;
    
    @Column(name = "medications", nullable = false)
    private String medications;
    
    @Column(name = "allergies", nullable = false)
    private String allergies;
    
    public MedicalHistory(){}
    
    public long getHistoryId(){
        return historyId;
    }
    
    public Patient getPatientId(){
        return patientId;
    }
    public String getFamilyHistory(){
        return familyHistory;
    }
    
    public String getPreExistingConditions(){
        return preExistingConditions;
    }
    
    public String getMedications(){
        return medications;
    }
    
    public String getAllergies(){
        return allergies;
    }
   
    public void setHistoryId(long historyId){
        this.historyId = historyId;
    }
    
    public void setPatient(Patient patientId){
        this.patientId = patientId;
    }
    
    public void setFamilyHistory(String familyHistory){
        this.familyHistory = familyHistory;
    }
    
    public void setPreExistingConditions(String preExistingConditions){
        this.preExistingConditions = preExistingConditions;
    }
    
    public void setMedications(String medications){
        this.medications = medications;
    }
    
    public void setAllergies(String allergies){
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "MedicalHistory{" + "historyId=" + historyId + ", patientId=" + patientId + ", familyHistory=" + familyHistory + ", preExistingConditions=" + preExistingConditions + ", medications=" + medications + ", allergies=" + allergies + '}';
    }
    
}

