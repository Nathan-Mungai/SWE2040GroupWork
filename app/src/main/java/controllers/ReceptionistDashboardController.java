// ReceptionistDashboardController.java
package controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.util.Duration;
import models.Doctor;
import models.Patient;
import models.Receptionist;
import models.Queue;
import models.QueuedPatient;
import services.DoctorService;
import services.PatientService;
import services.QueueService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceptionistDashboardController {

    private static final Logger LOGGER = Logger.getLogger(ReceptionistDashboardController.class.getName());

    @FXML private Label welcomeLabel;
    @FXML private Label systemTimeLabel;
    @FXML private Label connectedPatientsLabel;
    @FXML private Label lastUpdateLabel;
    @FXML private Button dashboardButton;
    @FXML private Button patientsButton;
    @FXML private Button queueButton;
    @FXML private Button settingsButton;
    @FXML private Button logoutButton;
    @FXML private Button refreshButton;
    @FXML private Button addPatientButton;
    @FXML private Button removeFromQueueButton;
    @FXML private TextField searchPatientField;
    @FXML private TableView<QueuedPatient> patientTable;
    @FXML private TableColumn<QueuedPatient, Long> idColumn;
    @FXML private TableColumn<QueuedPatient, String> fullNameColumn;
    @FXML private TableColumn<QueuedPatient, String> genderColumn;
    @FXML private TableColumn<QueuedPatient, Integer> ageColumn;
    @FXML private TableColumn<QueuedPatient, String> phoneColumn;
    @FXML private TableColumn<QueuedPatient, LocalDateTime> queuedAtColumn;

    private final PatientService patientService = new PatientService();
    private final DoctorService doctorService = new DoctorService();
    private final QueueService queueService = new QueueService();
    private ObservableList<QueuedPatient> queuedPatientsData = FXCollections.observableArrayList();
    private Receptionist currentReceptionist;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                systemTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm:ss a")));
            }), new KeyFrame(Duration.seconds(1)));
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();

            idColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
            fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            queuedAtColumn.setCellValueFactory(new PropertyValueFactory<>("queuedAt"));
            
            patientTable.setItems(queuedPatientsData);
            // Initially load the full list of patients
            loadAllPatients();
        });
    }

    public void setReceptionist(Receptionist receptionist) {
        this.currentReceptionist = receptionist;
        welcomeLabel.setText("Welcome, " + receptionist.getFullName());
    }

    private void loadAllPatients() {
        new Thread(() -> {
            try {
                List<Patient> patients = patientService.getAllPatients();
                List<QueuedPatient> allPatients = patients.stream()
                    .map(p -> new QueuedPatient(p, null))
                    .collect(Collectors.toList());
                Platform.runLater(() -> {
                    queuedPatientsData.setAll(allPatients);
                    lastUpdateLabel.setText("Last Update: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    connectedPatientsLabel.setText("Connected Patients: " + allPatients.size());
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load patients", e);
                Platform.runLater(() -> {
                    showAlert("Error", "Failed to load patients: " + e.getMessage());
                });
            }
        }).start();
    }

    private void loadQueuedPatients() {
        if (currentReceptionist == null || currentReceptionist.getAssignedDoctor() == null) {
            Platform.runLater(() -> showAlert("Error", "No doctor assigned to this receptionist. Cannot load queue."));
            return;
        }

        new Thread(() -> {
            try {
                // Fetch all patients once and create a map for efficient lookups
                List<Patient> allPatients = patientService.getAllPatients();
                Map<Long, Patient> patientMap = allPatients.stream()
                    .collect(Collectors.toMap(Patient::getPatientId, p -> p));

                // Fetch the list of queue items for the assigned doctor
                List<Queue> queueItems = queueService.getQueueByDoctor(currentReceptionist.getAssignedDoctor().getDoctorId());
                List<QueuedPatient> allQueuedPatients = new ArrayList<>();
                
                // Combine queue and patient data
                for (Queue item : queueItems) {
                    if (item != null) {
                        // DIAGNOSTIC LOG: This will show you if the patientId is null
                        LOGGER.log(Level.INFO, "Processing queue item with queueId: " + item.getQueueId() + " and patientId: " + item.getPatientId());
                        
                        Patient patient = patientMap.get(item.getPatientId());
                        if (patient != null) {
                            allQueuedPatients.add(new QueuedPatient(patient, item));
                        }
                    }
                }
                Platform.runLater(() -> {
                    queuedPatientsData.setAll(allQueuedPatients);
                    connectedPatientsLabel.setText("Patients in Queue: " + allQueuedPatients.size());
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load queue", e);
                Platform.runLater(() -> {
                    showAlert("Error", "Failed to load queue: " + e.getMessage());
                });
            }
        }).start();
    }
    
    @FXML
    private void handleRefresh() {
        loadAllPatients();
        showAlert("Info", "Patient list refreshed.");
    }

    @FXML
    private void handleAddPatient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/doctorsplaza/fxml/PatientRegistration.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Register New Patient");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            loadAllPatients();
        } catch (IOException e) {
            showAlert("Error", "Failed to open patient registration window: " + e.getMessage());
        }
    }

    @FXML
    private void handleRemoveFromQueue() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            showAlert("Selection Error", "Please select a patient to remove from the queue.");
            return;
        }
        
        if (selectedPatient.getQueue() == null || selectedPatient.getQueue().getQueueId() == null) {
            showAlert("Error", "Selected patient is not in the queue.");
            return;
        }

        // Add a confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove " + selectedPatient.getFullName() + " from the queue?");
        confirm.setContentText("Are you sure you want to remove this patient from the queue? This action cannot be undone.");
        Optional<ButtonType> result = confirm.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                Long queueIdToRemove = selectedPatient.getQueue().getQueueId();
                LOGGER.log(Level.INFO, "Attempting to remove queue item with ID: " + queueIdToRemove);
                try {
                    queueService.removeFromQueue(queueIdToRemove);
                    Platform.runLater(() -> {
                        showAlert("Success", selectedPatient.getFullName() + " has been removed from the queue.");
                        loadQueuedPatients();
                    });
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to remove patient from queue", e);
                    Platform.runLater(() -> showAlert("Error", "Failed to remove patient from queue: " + e.getMessage()));
                }
            }).start();
        }
    }

    @FXML
    private void handleSearchPatient() {
        String query = searchPatientField.getText().trim();
        if (query.isEmpty()) {
            loadAllPatients();
            return;
        }

        new Thread(() -> {
            try {
                List<Patient> allPatients = patientService.getAllPatients();
                List<QueuedPatient> filteredPatients = allPatients.stream()
                        .filter(p -> p.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                                p.getEmail().toLowerCase().contains(query.toLowerCase()))
                        .map(p -> new QueuedPatient(p, null))
                        .collect(Collectors.toList());

                Platform.runLater(() -> {
                    queuedPatientsData.setAll(filteredPatients);
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to search patients", e);
                Platform.runLater(() -> showAlert("Error", "Failed to search patients: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleAddToMyQueue() {
        QueuedPatient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        
        if (selectedPatient == null || selectedPatient.getPatient() == null) {
            showAlert("Selection Error", "Please select a patient from the table to add to the queue.");
            return;
        }
        
        if (currentReceptionist == null || currentReceptionist.getAssignedDoctor() == null) {
            showAlert("Error", "No doctor is assigned to this receptionist. Cannot add to queue.");
            return;
        }
        
        new Thread(() -> {
            try {
                Queue newQueue = new Queue();
                newQueue.setPatientId(selectedPatient.getPatientId());
                newQueue.setDoctorId(currentReceptionist.getAssignedDoctor().getDoctorId());
                newQueue.setReceptionistId(currentReceptionist.getReceptionistId());
                newQueue.setQueuedAt(LocalDateTime.now());
                
                queueService.addToQueue(newQueue);
                Platform.runLater(() -> {
                    showAlert("Success", selectedPatient.getFullName() + " has been added to the queue.");
                    loadQueuedPatients();
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to add patient to queue", e);
                Platform.runLater(() -> showAlert("Error", "Failed to add patient to queue: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleDashboard() {
        loadAllPatients();
    }

    @FXML
    private void handlePatients() {
        loadAllPatients();
    }

    @FXML
    private void handleQueue() {
        loadQueuedPatients();
    }

    @FXML
    private void handleSettings() {
        showAlert("Info", "Settings view not implemented.");
    }

    @FXML
    private void handleLogout() {
        showAlert("Info", "Logout not implemented. A new scene would be loaded here.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") || title.equals("Info") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
