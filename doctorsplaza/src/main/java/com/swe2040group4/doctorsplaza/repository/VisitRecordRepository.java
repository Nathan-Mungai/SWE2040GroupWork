/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swe2040group4.doctorsplaza.repository;
import com.swe2040group4.doctorsplaza.entity.Doctor;
import com.swe2040group4.doctorsplaza.entity.Patient;
import com.swe2040group4.doctorsplaza.entity.VisitRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author akuecyel
 */
@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
    List<VisitRecord> findByPatientId(Patient patientId);
    List<VisitRecord> findByDoctorId(Doctor doctorId);
    List<VisitRecord> findByPatientId_PatientIdOrderByVisitDateDesc(Long patientId);
}
