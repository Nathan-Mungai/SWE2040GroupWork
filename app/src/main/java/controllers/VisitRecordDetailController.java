package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Doctor;
import models.Patient;
import models.VisitRecord;
import services.DoctorService;
import services.PatientService;
import services.VisitRecordService;

public class VisitRecordDetailController {

    @FXML private TextField patientNameField;
    @FXML private TextField visitDateField;
    @FXML private TextField doctorField;
    @FXML private TextArea symptomsField;
    @FXML private TextArea diagnosisField;
    @FXML private TextArea treatmentPlanField;
    @FXML private TextArea notesField;
    @FXML private Button editButton;
    @FXML private Button closeButton;

    private VisitRecord visitRecord;
    private boolean isEditable = false;
    private final VisitRecordService visitRecordService = new VisitRecordService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    /**
     * Sets the visit record for this controller and initializes the UI fields.
     * @param visitRecord The visit record to be displayed.
     */
    public void setVisitRecord(VisitRecord visitRecord) {
        this.visitRecord = visitRecord;
        setFieldsEditable(false);
        updateFields();
    }

    @FXML
    private void initialize() {
        // This method is called automatically after the FXML file has been loaded.
        // It's a good place for initial setup.
    }

    /**
     * Updates the UI fields with the data from the current visitRecord object.
     */
    private void updateFields() {
        
    }

    /**
     * Sets the editable status of the form fields.
     * @param editable true to make fields editable, false to make them read-only.
     */
    private void setFieldsEditable(boolean editable) {
        symptomsField.setEditable(editable);
        diagnosisField.setEditable(editable);
        treatmentPlanField.setEditable(editable);
        notesField.setEditable(editable);
        patientNameField.setEditable(false);
        visitDateField.setEditable(false);
        doctorField.setEditable(false);
    }

    /**
     * Handles the "Edit" and "Save" button click event.
     * Toggles between view and edit mode, and saves changes when in edit mode.
     */
    @FXML
    private void handleEdit() {
        if (!isEditable) {
            setFieldsEditable(true);
            editButton.setText("Save");
            isEditable = true;
        } else {
            if (validateForm()) {
                try {
                    visitRecord.setSymptoms(symptomsField.getText());
                    visitRecord.setDiagnosis(diagnosisField.getText());
                    visitRecord.setTreatmentPlan(treatmentPlanField.getText());
                    visitRecord.setNotes(notesField.getText());

                    VisitRecord updatedRecord = visitRecordService.updateVisitRecord(visitRecord.getRecordId(), visitRecord);
                    setVisitRecord(updatedRecord);
                    setFieldsEditable(false);
                    editButton.setText("Edit");
                    isEditable = false;
                    showAlert("Success", "Visit record updated successfully.");
                } catch (Exception e) {
                    showAlert("Error", "Failed to update visit record: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Handles the "Close" button click event, closing the window.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates the form fields before saving.
     * @return true if the form is valid, false otherwise.
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        if (symptomsField.getText().trim().isEmpty()) {
            errors.append("Symptoms are required.\n");
        }
        if (diagnosisField.getText().trim().isEmpty()) {
            errors.append("Diagnosis is required.\n");
        }
        if (treatmentPlanField.getText().trim().isEmpty()) {
            errors.append("Treatment Plan is required.\n");
        }
        if (errors.length() > 0) {
            showAlert("Validation Error", errors.toString());
            return false;
        }
        return true;
    }

    /**
     * Helper method to show an alert dialog.
     * @param title The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
