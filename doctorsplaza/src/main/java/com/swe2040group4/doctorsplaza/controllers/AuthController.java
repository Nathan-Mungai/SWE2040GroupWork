package com.swe2040group4.doctorsplaza.controllers;

import com.swe2040group4.doctorsplaza.dto.LoginRequest;
import com.swe2040group4.doctorsplaza.dto.LoginResponse;
import com.swe2040group4.doctorsplaza.entity.Doctor;
import com.swe2040group4.doctorsplaza.entity.Receptionist;
import com.swe2040group4.doctorsplaza.repository.DoctorRepository;
import com.swe2040group4.doctorsplaza.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Doctor doctor = doctorRepository.findByEmail(loginRequest.getEmail());
        if (doctor != null && doctor.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(doctor.getDoctorId(), doctor.getEmail(), "DOCTOR", null));
        }
        
        Receptionist receptionist = receptionistRepository.findByEmail(loginRequest.getEmail());
        if (receptionist != null && receptionist.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(receptionist.getReceptionistId(), receptionist.getEmail(), "RECEPTIONIST", receptionist.getDoctorId().getDoctorId()));
        }
        
        return ResponseEntity.status(401).body(null);
    }
}