package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Doctor;
import models.Patient;
import models.VisitRecord;
import services.VisitRecordService;
import util.DateUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PatientMedicalHistoryController {

    // FXML components for the UI
    @FXML private Label patientInfoLabel;
    @FXML private TableView<VisitRecord> medicalHistoryTable;
    @FXML private TableColumn<VisitRecord, LocalDateTime> visitDateColumn;
    @FXML private TableColumn<VisitRecord, String> doctorColumn;
    @FXML private TableColumn<VisitRecord, String> symptomsColumn;
    @FXML private TableColumn<VisitRecord, String> diagnosisColumn;
    @FXML private TableColumn<VisitRecord, String> treatmentPlanColumn;
    @FXML private Label totalVisitsLabel;
    @FXML private Label doctorsConsultedLabel;
    @FXML private Label lastVisitLabel;
    @FXML private Label familyHistoryLabel;
    @FXML private Label medicationsLabel;
    @FXML private Label preExistingConditionsLabel;
    @FXML private Label allergiesLabel;

    // Data and Services
    private Patient patient;
    private Doctor loggedInDoctor;
    private final VisitRecordService visitRecordService = new VisitRecordService();

    /**
     * Initializes the controller. This is called automatically after the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        // Set up the table columns to bind to the properties of the VisitRecord model
        visitDateColumn.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        symptomsColumn.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        treatmentPlanColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentPlan"));

        // Use a custom CellFactory to format the LocalDateTime object for display
        visitDateColumn.setCellFactory(column -> new javafx.scene.control.TableCell<VisitRecord, LocalDateTime>() {
            private final java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        // Use a custom CellFactory for the doctor name
        doctorColumn.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue().getDoctorId();
            return new javafx.beans.property.SimpleStringProperty(doctor != null ? doctor.getFullName() : "N/A");
        });
    }

    /**
     * This method is called from the parent controller to pass in the patient and doctor data.
     * It's the starting point for loading the medical history.
     * @param patient The patient whose medical history is to be displayed.
     * @param doctor The currently logged-in doctor.
     */
    public void initData(Patient patient, Doctor doctor) {
        this.patient = patient;
        this.loggedInDoctor = doctor;

        if (patient != null) {
            patientInfoLabel.setText("Patient: " + patient.getFullName() + " (ID: " + patient.getPatientId() + ")");
        } else {
            patientInfoLabel.setText("Patient: N/A");
        }

        // Load the medical history asynchronously
        loadMedicalHistory();
    }

    /**
     * Fetches the patient's medical history from the API and populates the table and stats.
     */
    private void loadMedicalHistory() {
        if (patient == null) {
            return;
        }

        // Use CompletableFuture to run the API call on a background thread
        CompletableFuture.supplyAsync(() -> {
            try {
                // Assuming getVisitRecordsByPatient returns a List<VisitRecord>
                return visitRecordService.getVisitRecordsByPatient(patient.getPatientId());
            } catch (Exception e) {
                System.err.println("Failed to fetch visit records: " + e.getMessage());
                return new java.util.ArrayList<VisitRecord>(); // Return an empty list on failure
            }
        }).thenAccept(records -> {
            // Update the UI on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                // FIX: Convert the List<VisitRecord> to an ObservableList for the TableView
                ObservableList<VisitRecord> observableRecords = FXCollections.observableArrayList(records);
                medicalHistoryTable.setItems(observableRecords);
                updateStatsLabels(observableRecords);
            });
        });
    }

    /**
     * Updates the summary statistics labels based on the fetched records.
     * @param records The list of VisitRecord objects.
     */
    private void updateStatsLabels(ObservableList<VisitRecord> records) {
        totalVisitsLabel.setText("Total Visits: " + records.size());

        if (records.isEmpty()) {
            doctorsConsultedLabel.setText("Doctors Consulted: 0");
            lastVisitLabel.setText("Last Visit: N/A");
            return;
        }

        // Calculate the number of unique doctors
        Set<Long> uniqueDoctorIds = records.stream()
                .map(record -> record.getDoctorId().getDoctorId())
                .collect(Collectors.toSet());
        doctorsConsultedLabel.setText("Doctors Consulted: " + uniqueDoctorIds.size());

        // Find the most recent visit date
        records.stream()
            .max(Comparator.comparing(VisitRecord::getVisitDate))
            .ifPresent(lastVisit ->
                lastVisitLabel.setText("Last Visit: " + DateUtil.format(lastVisit.getVisitDate()))
            );

        // For now, these are static labels. They will be updated once we have the data model for them.
        familyHistoryLabel.setText("Family History: N/A");
        medicationsLabel.setText("Medications: N/A");
        preExistingConditionsLabel.setText("Pre-existing Conditions: N/A");
        allergiesLabel.setText("Allergies: N/A");
    }

    /**
     * Handles the back button action, closing the current window.
     */
    @FXML
    private void handleBack() {
        Stage stage = (Stage) medicalHistoryTable.getScene().getWindow();
        stage.close();
    }

    // Placeholder methods for future implementation
    @FXML
    private void handleFilterChange() {
        // Not implemented yet
    }

    @FXML
    private void handleAddNewRecord() {
        // Not implemented yet, will open a new window to add a record
    }

    @FXML
    private void handleExportHistory() {
        // Not implemented yet
    }

    @FXML
    private void handleClearFilters() {
        // Not implemented yet
    }
}
