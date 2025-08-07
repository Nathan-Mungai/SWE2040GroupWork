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
@Table(name = "visit_record")
public class VisitRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patientId;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctorId;

    @ManyToOne
    @JoinColumn(name = "receptionist_id")
    private Receptionist receptionistId;

    @Column(name = "symptoms", nullable = false)
    private String symptoms;
    
    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;
    
    @Column(name = "treatment_plan", nullable = false)
    private String treatmentPlan;
    
    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;
    
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;
    
    public VisitRecord(){}
    
    //getters
    public Long getRecordId(){
        return recordId;
    }
    
    public Patient getPatientId(){
        return patientId;
    }
    
    public Doctor getDoctorId(){
        return doctorId;
    }
    
    public Receptionist getReceptionistId(){
        return receptionistId;
    }
    
    public String getSymptoms(){
        return symptoms;
    }
    
    public String getDiagnosis(){
        return diagnosis;
    }
    
    public String getTreatmentPlan(){
        return treatmentPlan;
    }
    
    public LocalDateTime getVisitDate(){
        return visitDate;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    
   
    public void setRecordId(Long recordId){
        this.recordId = recordId;
    }
    
    public void setPatientId(Patient patientId){
        this.patientId = patientId;
    }
    
    public void setDoctorId(Doctor doctorId){
        this.doctorId = doctorId;
    }
    
    public void setReceptionistId(Receptionist receptionistId){
        this.receptionistId = receptionistId;
    }
    
    public void setSymptoms(String symptoms){
        this.symptoms = symptoms;
    }
    
    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }
    
    public void setTreatmentPlan(String treatmentPlan){
        this.treatmentPlan = treatmentPlan;
    }
    
    public void setVisitDate(LocalDateTime visitDate){
        this.visitDate = visitDate;
    }

    @Override
    public String toString() {
        return "VisitRecord{" + "recordId=" + recordId + ", patientId=" + patientId + ", doctorId=" + doctorId + ", receptionistId=" + receptionistId + ", symptoms=" + symptoms + ", diagnosis=" + diagnosis + ", treatmentPlan=" + treatmentPlan + ", visitDate=" + visitDate + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
    
    
}
