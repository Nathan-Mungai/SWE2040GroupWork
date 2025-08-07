package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Doctor;
import models.Patient;
import models.VisitRecord;
import services.VisitRecordService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddVisitRecordController {

    private static final Logger LOGGER = Logger.getLogger(AddVisitRecordController.class.getName());

    // FXML fields now consistently named with 'Field' suffix
    @FXML private Label patientNameLabel;
    @FXML private Label doctorNameLabel; // Added the missing FXML for the doctor name label
    @FXML private TextArea symptomsField;
    @FXML private TextArea diagnosisField;
    @FXML private TextArea treatmentPlanField;
    @FXML private TextArea notesField;

    private Patient patient;
    private Doctor doctor;
    private final VisitRecordService visitRecordService = new VisitRecordService();

    /**
     * Initializes the controller. This method is called automatically after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // No initialization logic is needed here for now.
    }

    /**
     * Sets the patient and doctor for this controller and updates the UI.
     * This method combines the functionality of both initData and setPatientAndDoctor.
     * @param patient The patient for whom the record is being added.
     * @param doctor The doctor adding the record.
     */
    public void initData(Patient patient, Doctor doctor) {
        this.patient = patient;
        this.doctor = doctor;
        if (patient != null) {
            patientNameLabel.setText(patient.getFullName());
        }
        if (doctor != null) {
            doctorNameLabel.setText(doctor.getFullName());
        }
    }

    /**
     * Handles the "Save Record" button click.
     * Creates and saves a new visit record to the API.
     */
    @FXML
    private void handleSaveRecord(ActionEvent event) {
        // Collect data from the form using the correct field names
        String symptoms = symptomsField.getText();
        String diagnosis = diagnosisField.getText();
        String treatmentPlan = treatmentPlanField.getText();
        String notes = notesField.getText();

        // Validate that essential fields are not empty
        if (symptoms.isEmpty() || diagnosis.isEmpty() || treatmentPlan.isEmpty()) {
            showAlert("Validation Error", "Symptoms, Diagnosis, and Treatment Plan cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        // Create a new VisitRecord object
        VisitRecord newRecord = new VisitRecord();

        // FIX: Set the entire Patient and Doctor objects, not just their IDs
        newRecord.setPatientId(patient);
        newRecord.setDoctorId(doctor);
        
        newRecord.setVisitDate(LocalDateTime.now());
        newRecord.setSymptoms(symptoms);
        newRecord.setDiagnosis(diagnosis);
        newRecord.setTreatmentPlan(treatmentPlan);
        newRecord.setNotes(notes);

        // Use CompletableFuture for cleaner asynchronous handling
        CompletableFuture.supplyAsync(() -> {
            try {
                return visitRecordService.saveVisitRecord(newRecord);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to save visit record", e);
                return null;
            }
        }).thenAccept(savedRecord -> {
            javafx.application.Platform.runLater(() -> {
                if (savedRecord != null && savedRecord.getRecordId() != null) {
                    showAlert("Success", "Visit record saved successfully.", Alert.AlertType.INFORMATION);
                    clearForm();
                } else {
                    showAlert("Error", "Failed to save visit record. Please try again.", Alert.AlertType.ERROR);
                }
            });
        });
    }

    /**
     * Handles the "Cancel" button click, closing the window.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Clears all input fields in the form.
     */
    private void clearForm() {
        symptomsField.clear();
        diagnosisField.clear();
        treatmentPlanField.clear();
        notesField.clear();
    }

    /**
     * Closes the current stage (window).
     */
    private void closeWindow() {
        Stage stage = (Stage) symptomsField.getScene().getWindow();
        stage.close();
    }

    /**
     * Helper method to show an alert dialog.
     */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
