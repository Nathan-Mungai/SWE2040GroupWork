package services;

import models.VisitRecord;
import util.HttpClientUtil;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisitRecordService {

    private static final Logger LOGGER = Logger.getLogger(VisitRecordService.class.getName());

    /**
     * Fetches a list of visit records for a specific patient.
     * @param patientId The ID of the patient.
     * @return A list of VisitRecord objects.
     * @throws Exception if the HTTP request fails.
     */
    public List<VisitRecord> getVisitRecordsByPatient(Long patientId) throws Exception {
        String url = "http://localhost:8080/api/visit-records/patient/" + patientId;
        VisitRecord[] records = HttpClientUtil.get(url, VisitRecord[].class);
        return Arrays.asList(records);
    }

    /**
     * Saves a new visit record.
     * This method now correctly constructs the JSON with nested patient and doctor IDs
     * to match the server-side entity's @ManyToOne relationship.
     * @param visitRecord The VisitRecord object to save.
     * @return The saved VisitRecord object.
     * @throws Exception if the HTTP request fails.
     */
    public VisitRecord saveVisitRecord(VisitRecord visitRecord) throws Exception {
        String url = "http://localhost:8080/api/visit-records";

        // Manually construct the JSON payload with nested objects for patientId and doctorId
        // FIX: We now correctly retrieve the long IDs from the nested objects
        String jsonPayload = String.format(
            "{\"patientId\": {\"patientId\": %d}, \"doctorId\": {\"doctorId\": %d}, \"visitDate\": \"%s\", \"symptoms\": \"%s\", \"diagnosis\": \"%s\", \"treatmentPlan\": \"%s\", \"notes\": \"%s\"}",
            visitRecord.getPatientId().getPatientId(), // Corrected call
            visitRecord.getDoctorId().getDoctorId(),   // Corrected call
            visitRecord.getVisitDate(),
            visitRecord.getSymptoms(),
            visitRecord.getDiagnosis(),
            visitRecord.getTreatmentPlan(),
            visitRecord.getNotes()
        );
        LOGGER.log(Level.INFO, "JSON Payload to be sent: {0}", jsonPayload);

        return HttpClientUtil.post(url, jsonPayload, VisitRecord.class);
    }

    /**
     * Updates an existing visit record.
     * @param recordId The ID of the record to update.
     * @param visitRecord The updated VisitRecord object.
     * @return The updated VisitRecord object.
     * @throws Exception if the HTTP request fails.
     */
    public VisitRecord updateVisitRecord(Long recordId, VisitRecord visitRecord) throws Exception {
        String url = "http://localhost:8080/api/visit-records/" + recordId;
        return HttpClientUtil.put(url, visitRecord, VisitRecord.class);
    }
}
