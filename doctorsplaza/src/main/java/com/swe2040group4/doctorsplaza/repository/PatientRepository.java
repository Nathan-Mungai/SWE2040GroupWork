/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swe2040group4.doctorsplaza.repository;

import com.swe2040group4.doctorsplaza.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 *
 * @author akuecyel
 */

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String email);
    Patient findByPhoneNumber(String phoneNumber);
    List<Patient> findByFirstNameAndLastName(String firstName, String lastName);
    Patient findByIdNumber(String idNumber);
    List<Patient> findByDateOfBirth(LocalDate dateOfBirth);
}
