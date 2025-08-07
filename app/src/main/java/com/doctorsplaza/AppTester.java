/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.doctorsplaza;

import java.util.List;
import services.PatientService;
import models.Patient;

public class AppTester {
    public static void main(String[] args) {
        PatientService patientService = new PatientService();
        try {
            System.out.println("Fetching all patients...");
            List<Patient> patients = patientService.getAllPatients();
            for (Patient p : patients) {
                System.out.println("Patient: " + p.getFullName() + ", ID: " + p.getPatientId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}