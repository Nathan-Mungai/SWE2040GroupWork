/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.controllers;

import com.swe2040group4.doctorsplaza.entity.MedicalHistory;
import com.swe2040group4.doctorsplaza.repository.MedicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @GetMapping
    public List<MedicalHistory> getAllHistories() {
        return medicalHistoryRepository.findAll();
    }

    @PostMapping
    public MedicalHistory createHistory(@RequestBody MedicalHistory medicalHistory) {
        return medicalHistoryRepository.save(medicalHistory);
    }
}
