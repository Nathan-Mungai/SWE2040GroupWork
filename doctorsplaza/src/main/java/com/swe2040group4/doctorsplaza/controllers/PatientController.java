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

    /**
     * Handles POST requests to save a new patient.
     * It first checks if a patient with the same email, phone number, or ID already exists.
     * If an existing patient is found, it returns that patient's data.
     * Otherwise, it saves the new patient to the database.
     * @param patient The patient object to save.
     * @return ResponseEntity containing the saved or existing Patient.
     */
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        // Check for existing patient by unique fields to prevent duplicates
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
        
        // If no existing patient is found, save the new one
        return ResponseEntity.ok(patientRepository.save(patient));
    }

    /**
     * Handles GET requests to retrieve all patients.
     * @return A list of all Patient objects from the database.
     */
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    /**
     * Handles GET requests to retrieve a single patient by their ID.
     * @param id The ID of the patient to retrieve.
     * @return The Patient object if found, or a 404 Not Found response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Handles GET requests to search for a patient by email, phone number, or ID number.
     * @param email The patient's email.
     * @param phoneNumber The patient's phone number.
     * @param idNumber The patient's ID number.
     * @return The Patient object if found, or a 404 Not Found response.
     */
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

    /**
     * Handles GET requests to search for a patient by their full name.
     * @param first The patient's first name.
     * @param last The patient's last name.
     * @return A list of patients matching the full name.
     */
    @GetMapping("/by-name")
    public ResponseEntity<List<Patient>> findByFullName(@RequestParam String first, @RequestParam String last) {
        return ResponseEntity.ok(patientRepository.findByFirstNameAndLastName(first, last));
    }
}
