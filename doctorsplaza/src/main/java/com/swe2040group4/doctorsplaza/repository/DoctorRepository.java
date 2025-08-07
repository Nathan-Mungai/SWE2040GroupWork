/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swe2040group4.doctorsplaza.repository;

import com.swe2040group4.doctorsplaza.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author akuecyel
 */

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmail(String email);
    Doctor findByPhoneNumber(String phoneNumber);
    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
    List<Doctor> findBySpecialization(String specialization);
    Doctor findByEmailAndPassword(String email, String password);
}
