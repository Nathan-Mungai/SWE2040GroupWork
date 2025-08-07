package com.swe2040group4.doctorsplaza.controllers;

import com.swe2040group4.doctorsplaza.entity.Receptionist;
import com.swe2040group4.doctorsplaza.repository.ReceptionistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/receptionists")
public class ReceptionistController {

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @GetMapping
    public List<Receptionist> getAllReceptionists() {
        return receptionistRepository.findAll();
    }

    @PostMapping
    public Receptionist createReceptionist(@RequestBody Receptionist receptionist) {
        return receptionistRepository.save(receptionist);
    }
     @GetMapping("/email/{email}")
    public ResponseEntity<Receptionist> getReceptionistByEmail(@PathVariable String email) {
        Receptionist receptionist = receptionistRepository.findByEmail(email);
        if (receptionist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receptionist);
    }
}