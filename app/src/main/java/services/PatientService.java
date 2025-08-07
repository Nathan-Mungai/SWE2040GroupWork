package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Patient;
import util.HttpClientUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientService {

    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    private static final String PATIENT_API_URL = "http://localhost:8080/api/patients";
    private final ObjectMapper objectMapper;
    // Removed the QueueService dependency since it will no longer be used
    // private final QueueService queueService;

    public PatientService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // Configure the ObjectMapper to serialize LocalDate as a string
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // this.queueService = new QueueService(); // Removed initialization
    }

    /**
     * Saves a new patient to the system. The queue functionality has been removed.
     * @param newPatient The patient object to save.
     * @return The saved Patient object with its ID.
     * @throws IOException if the registration fails due to a network or serialization issue.
     */
    public Patient savePatient(Patient newPatient) throws IOException, Exception {
        try {
            Map<String, Object> patientPayload = new HashMap<>();
            patientPayload.put("firstName", newPatient.getFirstName());
            patientPayload.put("lastName", newPatient.getLastName());
            patientPayload.put("gender", newPatient.getGender());
            patientPayload.put("dateOfBirth", newPatient.getDateOfBirth());
            patientPayload.put("phoneNumber", newPatient.getPhoneNumber());
            patientPayload.put("email", newPatient.getEmail());
            patientPayload.put("idNumber", newPatient.getIdNumber());
            patientPayload.put("emergencyContactName", newPatient.getEmergencyContactName());
            patientPayload.put("emergencyContactNumber", newPatient.getEmergencyContactNumber());
            patientPayload.put("insuranceProvider", newPatient.getInsuranceProvider());
            patientPayload.put("insuranceNumber", newPatient.getInsuranceNumber());

            String jsonPayload = objectMapper.writeValueAsString(patientPayload);
            LOGGER.log(Level.INFO, "Sending JSON payload: " + jsonPayload);

            String responseBody = HttpClientUtil.post(PATIENT_API_URL, jsonPayload);
            
            Patient savedPatient = objectMapper.readValue(responseBody, Patient.class);
            
            LOGGER.log(Level.INFO, "Patient saved successfully with ID: " + savedPatient.getPatientId());
            
            // The code to add the patient to the queue has been removed.
            
            return savedPatient;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save patient. Check network connection or server status.", e);
            throw e;
        }
    }
    
    /**
     * Retrieves a patient by their ID.
     * @param patientId The ID of the patient to retrieve.
     * @return The Patient object.
     * @throws IOException if the API call fails.
     */
    public Patient getPatientById(Long patientId) throws IOException, InterruptedException {
        String url = PATIENT_API_URL + "/" + patientId;
        String jsonResponse = HttpClientUtil.get(url);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            return objectMapper.readValue(jsonResponse, Patient.class);
        }
        return null;
    }

    /**
     * Retrieves all patients from the system.
     * @return A list of Patient objects.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    public List<Patient> getAllPatients() throws IOException, InterruptedException {
        String jsonResponse = HttpClientUtil.get(PATIENT_API_URL);
        return objectMapper.readValue(jsonResponse, new TypeReference<List<Patient>>() {});
    }
}
