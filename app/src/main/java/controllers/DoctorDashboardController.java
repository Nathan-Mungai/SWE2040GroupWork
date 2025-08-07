package controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Doctor;
import models.Patient;
import models.QueuedPatient;
import models.Queue;
import services.DoctorService;
import services.PatientService;
import services.QueueService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DoctorDashboardController {

    private static final Logger LOGGER = Logger.getLogger(DoctorDashboardController.class.getName());

    // FXML elements from the dashboard view
    @FXML private Label welcomeLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label queueStatusLabel;
    @FXML private Label statusLabel;
    @FXML private Label lastUpdateLabel;
    @FXML private Label connectedPatientsLabel;
    @FXML private Label systemTimeLabel;


    @FXML private TableView<QueuedPatient> patientTable;
    @FXML private TableColumn<QueuedPatient, Long> idColumn;
    @FXML private TableColumn<QueuedPatient, String> fullNameColumn;
    @FXML private TableColumn<QueuedPatient, String> genderColumn;
    @FXML private TableColumn<QueuedPatient, String> ageColumn;
    @FXML private TableColumn<QueuedPatient, String> phoneColumn;
    @FXML private TableColumn<QueuedPatient, String> statusColumn;
    @FXML private TableColumn<QueuedPatient, LocalDateTime> queuedColumn;
    @FXML private TableColumn<QueuedPatient, String> lastVisitColumn;

    // Buttons
    @FXML private Button dashboardButton;
    @FXML private Button myPatientsButton;
    @FXML private Button appointmentsButton;
    @FXML private Button settingsButton;
    @FXML private Button logoutButton;
    @FXML private Button refreshButton;
    @FXML private Button viewMedicalHistoryButton;
    @FXML private Button addVisitRecordButton;
    @FXML private Button markAsSeenButton;
    @FXML private Button removeFromQueueButton;

    // Services for API calls
    private final QueueService queueService = new QueueService();
    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();

    private final ObservableList<QueuedPatient> queueData = FXCollections.observableArrayList();

    // The currently logged-in doctor.
    private Doctor loggedInDoctor;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        ageColumn.setCellValueFactory(cellData -> {
            LocalDate dob = cellData.getValue().getPatient().getDateOfBirth();
            if (dob != null) {
                int age = Period.between(dob, LocalDate.now()).getYears();
                return new SimpleStringProperty(String.valueOf(age));
            }
            return new SimpleStringProperty("N/A");
        });

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("patientStatus"));
        queuedColumn.setCellValueFactory(new PropertyValueFactory<>("queuedAt"));

        lastVisitColumn.setCellValueFactory(cellData -> {
            LocalDateTime lastVisit = cellData.getValue().getPatient().getLastVisit();
            if (lastVisit != null) {
                return new SimpleStringProperty(lastVisit.toString());
            }
            return new SimpleStringProperty("N/A");
        });

        patientTable.setItems(queueData);

        patientTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                boolean isSelected = (newValue != null);
                viewMedicalHistoryButton.setDisable(!isSelected);
                addVisitRecordButton.setDisable(!isSelected);
                markAsSeenButton.setDisable(!isSelected);
                removeFromQueueButton.setDisable(!isSelected);
            });

        viewMedicalHistoryButton.setDisable(true);
        addVisitRecordButton.setDisable(true);
        markAsSeenButton.setDisable(true);
        removeFromQueueButton.setDisable(true);
    }

    public void setDoctor(Doctor doctor) {
        this.loggedInDoctor = doctor;
        if (doctor != null) {
            welcomeLabel.setText("Welcome, Dr. " + doctor.getFullName() + "!");
            loadPatientQueue();
        }
    }

    @FXML
    private void handleDashboard() {
        loadPatientQueue();
        showAlert("Info", "Dashboard refreshed.");
    }

    @FXML
    private void handleMyPatients() {
        showAlert("Info", "My Patients view not implemented.");
    }

    @FXML
    private void handleAppointments() {
        showAlert("Info", "Appointments view not implemented.");
    }

    @FXML
    private void handleSettings() {
        showAlert("Info", "Settings view not implemented.");
    }

    @FXML
    private void handleLogout() {
        showAlert("Info", "Logout not implemented.");
    }

    @FXML
    private void handleQueueFilter() {
        showAlert("Info", "Queue filter not implemented.");
    }

    @FXML
    private void handleRefresh() {
        loadPatientQueue();
        showAlert("Success", "Patient queue refreshed.");
    }

    @FXML
    private void handleViewMedicalHistory() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/doctorsplaza/fxml/PatientMedicalHistory.fxml"));
                Parent root = fxmlLoader.load();
                PatientMedicalHistoryController controller = fxmlLoader.getController();
                controller.initData(selectedPatient.getPatient(), loggedInDoctor);

                Stage stage = new Stage();
                stage.setTitle("Medical History for " + selectedPatient.getFullName());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to open medical history window", e);
                showAlert("Error", "Failed to open medical history window: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select a patient to view their medical history.");
        }
    }

    @FXML
    private void handleAddVisitRecord() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            try {
                // Use a more robust way to get the FXML resource.
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/doctorsplaza/fxml/AddVisitRecord.fxml"));
                Parent root = fxmlLoader.load();
                
                // Get the controller for the new window and pass the patient and doctor data.
               AddVisitRecordController controller = fxmlLoader.getController();
                controller.initData(selectedPatient.getPatient(), loggedInDoctor);

                Stage stage = new Stage();
                stage.setTitle("Add Visit Record for " + selectedPatient.getFullName());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Refresh the patient queue after the modal window is closed.
                loadPatientQueue();
            } catch (IOException e) {
                // Log the exception for debugging purposes.
                LOGGER.log(Level.SEVERE, "Failed to open Add Visit Record window", e);
                // Show a user-friendly alert.
                showAlert("Error", "Failed to open Add Visit Record window: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select a patient to add a visit record.");
        }
    }

    @FXML
    private void handleMarkAsSeen() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    queueService.markPatientAsSeen(selectedPatient);
                    Platform.runLater(() -> {
                        showAlert("Success", selectedPatient.getFullName() + " has been marked as seen and removed from the queue.");
                        loadPatientQueue();
                    });
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to mark patient as seen", e);
                    Platform.runLater(() -> showAlert("Error", "Failed to mark patient as seen: " + e.getMessage()));
                }
            });
        } else {
            showAlert("Error", "Please select a patient to mark as seen.");
        }
    }

    @FXML
    private void handleRemoveFromQueue() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    queueService.removeFromQueue(selectedPatient.getQueueId());
                    Platform.runLater(() -> {
                        showAlert("Success", "Patient removed from queue successfully.");
                        loadPatientQueue();
                    });
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to remove patient from queue", e);
                    Platform.runLater(() -> showAlert("Error", "Failed to remove patient from queue: " + e.getMessage()));
                }
            });
        } else {
            showAlert("Error", "Please select a patient to remove.");
        }
    }

    private void loadPatientQueue() {
        if (loggedInDoctor == null) {
            showAlert("Error", "No doctor logged in.");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                List<Queue> queues = queueService.getQueueByDoctor(loggedInDoctor.getDoctorId());

                if (queues == null || queues.isEmpty()) {
                    Platform.runLater(() -> {
                        queueData.clear();
                        queueStatusLabel.setText("Showing: My Queue (0 patients)");
                    });
                    return;
                }

                List<CompletableFuture<Patient>> patientFutures = queues.stream()
                    .filter(queueItem -> queueItem != null && queueItem.getPatientId() != null)
                    .map(queueItem -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return patientService.getPatientById(queueItem.getPatientId());
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Failed to fetch patient details for ID: " + queueItem.getPatientId(), e);
                            return null;
                        }
                    }))
                    .collect(Collectors.toList());

                List<Patient> patients = patientFutures.stream()
                    .map(CompletableFuture::join)
                    .filter(patient -> patient != null)
                    .collect(Collectors.toList());

                List<QueuedPatient> queuedPatients = new ArrayList<>();
                for (Queue queueItem : queues) {
                    patients.stream()
                        .filter(patient -> patient.getPatientId().equals(queueItem.getPatientId()))
                        .findFirst()
                        .ifPresent(patient -> queuedPatients.add(new QueuedPatient(patient, queueItem)));
                }

                LOGGER.log(Level.INFO, "API returned queue list: " + queuedPatients);

                Platform.runLater(() -> {
                    queueData.setAll(queuedPatients);
                    queueStatusLabel.setText("Showing: My Queue (" + queuedPatients.size() + " patients)");
                    if (queuedPatients.isEmpty()) {
                        showAlert("Info", "Your patient queue is currently empty.");
                    }
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load patient queue", e);
                Platform.runLater(() -> showAlert("Error", "Failed to load patient queue: " + e.getMessage()));
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
