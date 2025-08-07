// QueueController.java
package com.swe2040group4.doctorsplaza.controllers;

import com.swe2040group4.doctorsplaza.dto.QueueRequestDto;
import com.swe2040group4.doctorsplaza.dto.QueueResponseDto;
import com.swe2040group4.doctorsplaza.entity.Doctor;
import com.swe2040group4.doctorsplaza.entity.Patient;
import com.swe2040group4.doctorsplaza.entity.Queue;
import com.swe2040group4.doctorsplaza.entity.Receptionist;
import com.swe2040group4.doctorsplaza.repository.DoctorRepository;
import com.swe2040group4.doctorsplaza.repository.PatientRepository;
import com.swe2040group4.doctorsplaza.repository.QueueRepository;
import com.swe2040group4.doctorsplaza.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/queues")
public class QueueController {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<Queue>> getAllQueues() {
        return ResponseEntity.ok(queueRepository.findAll());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<QueueResponseDto>> getQueueByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        List<Queue> queues = queueRepository.findByDoctor(doctor);
        List<QueueResponseDto> queueDtos = queues.stream()
                .map(QueueResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(queueDtos);
    }

    @PostMapping
    public ResponseEntity<Queue> createQueue(@RequestBody QueueRequestDto queueRequest) {
        Patient patient = patientRepository.findById(queueRequest.getPatientId()).orElse(null);
        Doctor doctor = doctorRepository.findById(queueRequest.getDoctorId()).orElse(null);
        Receptionist receptionist = receptionistRepository.findById(queueRequest.getReceptionistId()).orElse(null);

        if (patient == null || doctor == null || receptionist == null) {
            return ResponseEntity.notFound().build();
        }

        Queue newQueue = new Queue();
        newQueue.setPatient(patient);
        newQueue.setDoctor(doctor);
        newQueue.setReceptionist(receptionist);
        newQueue.setQueuedAt(LocalDateTime.now());
        newQueue.setStatus(Queue.Status.Waiting);

        return ResponseEntity.ok(queueRepository.save(newQueue));
    }

    @PutMapping("/{queueId}/status")
    public ResponseEntity<Queue> updateQueueStatus(@PathVariable Long queueId, @RequestBody Queue.Status status) {
        Queue queue = queueRepository.findById(queueId).orElse(null);
        if (queue == null) {
            return ResponseEntity.notFound().build();
        }
        queue.setStatus(status);
        return ResponseEntity.ok(queueRepository.save(queue));
    }

    @DeleteMapping("/{queueId}")
    public ResponseEntity<Void> removeFromQueue(@PathVariable Long queueId) {
        if (queueRepository.existsById(queueId)) {
            queueRepository.deleteById(queueId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
