/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swe2040group4.doctorsplaza.repository;
import com.swe2040group4.doctorsplaza.entity.Doctor;
import com.swe2040group4.doctorsplaza.entity.Receptionist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author akuecyel
 */
@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Receptionist findByEmail(String email);
    List<Receptionist> findByFirstNameAndLastName(String firstName, String lastName);
    List<Receptionist> findByDoctorId(Doctor doctorId);
}
