package com.swe2040group4.doctorsplaza.controllers;


import com.swe2040group4.doctorsplaza.entity.VisitRecord;
import com.swe2040group4.doctorsplaza.repository.VisitRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/visit-records")
@CrossOrigin(origins = "*")
public class VisitRecordController {

    @Autowired
    private VisitRecordRepository visitRecordRepository;
    
    

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitRecord>> getVisitRecordsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(visitRecordRepository.findByPatientId_PatientIdOrderByVisitDateDesc(patientId));
    }

    @PostMapping
    public ResponseEntity<VisitRecord> createVisitRecord(@RequestBody VisitRecord visitRecord) {
        visitRecord.setVisitDate(LocalDateTime.now());
        return ResponseEntity.ok(visitRecordRepository.save(visitRecord));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<VisitRecord> updateVisitRecord(@PathVariable Long recordId, @RequestBody VisitRecord updatedRecord) {
        VisitRecord existingRecord = visitRecordRepository.findById(recordId).orElse(null);
        if (existingRecord == null) {
            return ResponseEntity.notFound().build();
        }
        
        existingRecord.setSymptoms(updatedRecord.getSymptoms());
        existingRecord.setDiagnosis(updatedRecord.getDiagnosis());
        existingRecord.setTreatmentPlan(updatedRecord.getTreatmentPlan());
        return ResponseEntity.ok(visitRecordRepository.save(existingRecord));
    }
}