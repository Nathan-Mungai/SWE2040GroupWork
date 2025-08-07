/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swe2040group4.doctorsplaza.dto;
public class DoctorLoginResponse {
    private Long doctorId;
    private String email;
    private String role;

    public DoctorLoginResponse(Long doctorId, String email, String role) {
        this.doctorId = doctorId;
        this.email = email;
        this.role = role;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
