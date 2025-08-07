package com.swe2040group4.doctorsplaza.controllers;

import com.swe2040group4.doctorsplaza.entity.Patient;
import com.swe2040group4.doctorsplaza.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient existing = patientRepository.findByEmail(patient.getEmail());
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        existing = patientRepository.findByPhoneNumber(patient.getPhoneNumber());
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        existing = patientRepository.findByIdNumber(patient.getIdNumber());
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.ok(patientRepository.save(patient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Patient> searchPatient(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String idNumber) {

        if (email != null) {
            Patient patient = patientRepository.findByEmail(email);
            return patient != null ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
        } else if (phoneNumber != null) {
            Patient patient = patientRepository.findByPhoneNumber(phoneNumber);
            return patient != null ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
        } else if (idNumber != null) {
            Patient patient = patientRepository.findByIdNumber(idNumber);
            return patient != null ? ResponseEntity.ok(patient) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<Patient>> findByFullName(@RequestParam String first, @RequestParam String last) {
        return ResponseEntity.ok(patientRepository.findByFirstNameAndLastName(first, last));
    }
}