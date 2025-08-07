// Queue.java
package com.swe2040group4.doctorsplaza.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue")
@JsonIgnoreProperties(value = {"doctor", "patient", "receptionist"})
public class Queue {

    public enum Status {
        Waiting,
        Seen,
        Cancelled
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "receptionist_id")
    private Receptionist receptionist;

    @Column(name = "queued_at", nullable = false)
    private LocalDateTime queuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Queue(){}

    // Getters and Setters with corrected names

    public Long getQueueId(){
        return queueId;
    }

    public void setQueueId(Long queueId){
        this.queueId = queueId;
    }

    public Doctor getDoctor(){
        return doctor;
    }

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    public Patient getPatient(){
        return patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public Receptionist getReceptionist(){
        return receptionist;
    }

    public void setReceptionist(Receptionist receptionist){
        this.receptionist = receptionist;
    }

    public LocalDateTime getQueuedAt(){
        return queuedAt;
    }

    public void setQueuedAt(LocalDateTime queuedAt){
        this.queuedAt = queuedAt;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    @Override
    public String toString() {
        return "Queue{" + "queueId=" + queueId + ", doctor=" + doctor.getDoctorId() + ", patient=" + patient.getPatientId() + ", receptionist=" + receptionist.getReceptionistId() + ", queuedAt=" + queuedAt + ", status=" + status + '}';
    }
}
