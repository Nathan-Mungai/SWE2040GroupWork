package services;

import models.Doctor;
import util.HttpClientUtil;

import java.util.Arrays;
import java.util.List;

public class DoctorService {
    public List<Doctor> getAllDoctors() throws Exception {
        Doctor[] doctors = HttpClientUtil.get(
                "http://localhost:8080/api/doctors",
                Doctor[].class);
        return Arrays.asList(doctors);
    }

    public Doctor getDoctorById(Long doctorId) throws Exception {
        String url = "http://localhost:8080/api/doctors/" + doctorId;
        return HttpClientUtil.get(url, Doctor.class);
    }

    public Doctor createDoctor(Doctor doctor) throws Exception {
        return HttpClientUtil.post(
                "http://localhost:8080/api/doctors",
                doctor,
                Doctor.class);
    }
    public Doctor getDoctorByEmail(String email) throws Exception {
        String url = "http://localhost:8080/api/doctors/email/" + email;
        return HttpClientUtil.get(url, Doctor.class);
    }
}
