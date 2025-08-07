package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Patient;
import models.Queue;
import util.HttpClientUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientService {

    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    private static final String PATIENT_API_URL = "http://localhost:8080/api/patients";
    private final ObjectMapper objectMapper;
    private final QueueService queueService;

    public PatientService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // FIX: Configure the ObjectMapper to serialize LocalDate as a string
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.queueService = new QueueService();
    }

    /**
     * Saves a new patient to the system and adds them to a default queue.
     * @param newPatient The patient object to save.
     * @return The saved Patient object with its ID.
     * @throws Exception if the registration fails.
     */
    public Patient savePatient(Patient newPatient) throws Exception {
        try {
            // FIX: Create a Map with only the fields required for registration.
            // This guarantees that no calculated fields like 'age' or 'fullName' are sent.
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
            patientPayload.put("address", newPatient.getAddress());

            // Send the new patient data to the backend
            String jsonPayload = objectMapper.writeValueAsString(patientPayload);
            String responseBody = HttpClientUtil.post(PATIENT_API_URL, jsonPayload);
            Patient savedPatient = objectMapper.readValue(responseBody, Patient.class);
            
            LOGGER.log(Level.INFO, "Patient saved successfully with ID: " + savedPatient.getPatientId());
            
            // Immediately add the new patient to the queue for a default doctor (ID 3 in this example)
            if (savedPatient.getPatientId() != null) {
                Queue newQueueItem = new Queue();
                newQueueItem.setPatientId(savedPatient.getPatientId());
                newQueueItem.setDoctorId(3L);
                
                queueService.addToQueue(newQueueItem);
                LOGGER.log(Level.INFO, "Patient with ID " + savedPatient.getPatientId() + " added to queue for doctor 3.");
            } else {
                LOGGER.log(Level.WARNING, "Patient ID is null after saving, cannot add to queue.");
            }

            return savedPatient;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save patient or add to queue.", e);
            throw new Exception("Failed to register patient: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves a patient by their ID.
     * @param patientId The ID of the patient to retrieve.
     * @return The Patient object.
     * @throws Exception if the API call fails.
     */
    public Patient getPatientById(Long patientId) throws Exception {
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
     * @throws InterruptedException if the operation is interrupted.
     */
    public List<Patient> getAllPatients() throws IOException, InterruptedException {
        String jsonResponse = HttpClientUtil.get(PATIENT_API_URL);
        return objectMapper.readValue(jsonResponse, new TypeReference<List<Patient>>() {});
    }
}
