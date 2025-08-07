package com.swe2040group4.doctorsplaza.dto;

import com.swe2040group4.doctorsplaza.entity.Queue;

import java.time.LocalDateTime;

public class QueueResponseDto {
    private Long queueId;
    private Long patientId;
    private LocalDateTime queuedAt;
    private Queue.Status status;

    // Default constructor
    public QueueResponseDto() {
    }

    // Constructor to map from the Queue entity
    public QueueResponseDto(Queue queue) {
        this.queueId = queue.getQueueId();
        this.patientId = queue.getPatient().getPatientId();
        this.queuedAt = queue.getQueuedAt();
        this.status = queue.getStatus();
    }

    // Getters and setters
    public Long getQueueId() {
        return queueId;
    }

    public void setQueueId(Long queueId) {
        this.queueId = queueId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getQueuedAt() {
        return queuedAt;
    }

    public void setQueuedAt(LocalDateTime queuedAt) {
        this.queuedAt = queuedAt;
    }

    public Queue.Status getStatus() {
        return status;
    }

    public void setStatus(Queue.Status status) {
        this.status = status;
    }
}