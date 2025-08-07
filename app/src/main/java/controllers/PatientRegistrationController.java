package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Patient;
import services.PatientService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientRegistrationController {

    private static final Logger LOGGER = Logger.getLogger(PatientRegistrationController.class.getName());
    
    @FXML private VBox rootVBox; 
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private DatePicker dobDatePicker;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField idNumberField;
    @FXML private TextField emergencyContactNameField;
    @FXML private TextField emergencyContactNumberField;
    @FXML private TextField insuranceProviderField;
    @FXML private TextField insuranceNumberField;

    private final PatientService patientService = new PatientService();

    @FXML
    public void initialize() {
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        Patient patient = new Patient();
        patient.setFirstName(firstNameField.getText());
        patient.setLastName(lastNameField.getText());
        patient.setGender(genderComboBox.getValue().toUpperCase());
        patient.setDateOfBirth(dobDatePicker.getValue());
        patient.setPhoneNumber(phoneNumberField.getText());
        patient.setEmail(emailField.getText());
        patient.setIdNumber(idNumberField.getText());
        patient.setEmergencyContactName(emergencyContactNameField.getText());
        patient.setEmergencyContactNumber(emergencyContactNumberField.getText());
        patient.setInsuranceProvider(insuranceProviderField.getText());
        patient.setInsuranceNumber(insuranceNumberField.getText());

        new Thread(() -> {
            try {
                Patient savedPatient = patientService.savePatient(patient);
                javafx.application.Platform.runLater(() -> {
                    showAlert("Success", "Patient " + savedPatient.getFullName() + " registered successfully!");
                    clearFields();
                    // Removed the call to handleCancel() here to prevent a NullPointerException
                    // The user will now need to manually click the Cancel button to close the window
                    // or a new function can be implemented here.
                });
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to register patient", e);
                javafx.application.Platform.runLater(() -> showAlert("Error", "Failed to register patient: " + e.getMessage()));
            } catch (Exception ex) {
                Logger.getLogger(PatientRegistrationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    @FXML
    private void handleCancel() {
        // Use a reliably injected field (like a TextField) to get the Scene and then the Stage.
        // This is a more robust way to handle closing the window.
        if (firstNameField != null && firstNameField.getScene() != null && firstNameField.getScene().getWindow() instanceof Stage) {
            Stage stage = (Stage) firstNameField.getScene().getWindow();
            stage.close();
        } else {
            LOGGER.log(Level.WARNING, "Failed to close the window. The firstNameField, scene, or stage was not available.");
        }
    }

    private boolean validateInput() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || genderComboBox.getValue() == null ||
            dobDatePicker.getValue() == null || phoneNumberField.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields (First Name, Last Name, Gender, Date of Birth, Phone Number).");
            return false;
        }
        return true;
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        dobDatePicker.setValue(null);
        phoneNumberField.clear();
        emailField.clear();
        idNumberField.clear();
        emergencyContactNameField.clear();
        emergencyContactNumberField.clear();
        insuranceProviderField.clear();
        insuranceNumberField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
