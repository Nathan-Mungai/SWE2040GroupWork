package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Patient;
import services.PatientService;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientRegistrationController {

    private static final Logger LOGGER = Logger.getLogger(PatientRegistrationController.class.getName());

    @FXML private AnchorPane patientRegistrationAnchorPane;
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
    @FXML private TextArea addressArea;

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
        patient.setGender(genderComboBox.getValue());
        patient.setDateOfBirth(dobDatePicker.getValue());
        patient.setPhoneNumber(phoneNumberField.getText());
        patient.setEmail(emailField.getText());
        patient.setIdNumber(idNumberField.getText());
        patient.setEmergencyContactName(emergencyContactNameField.getText());
        patient.setEmergencyContactNumber(emergencyContactNumberField.getText());
        patient.setInsuranceProvider(insuranceProviderField.getText());
        patient.setInsuranceNumber(insuranceNumberField.getText());
        patient.setAddress(addressArea.getText());

        new Thread(() -> {
            try {
                Patient savedPatient = patientService.savePatient(patient);
                javafx.application.Platform.runLater(() -> {
                    showAlert("Success", "Patient " + savedPatient.getFullName() + " registered successfully!");
                    clearFields();
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to register patient", e);
                javafx.application.Platform.runLater(() -> showAlert("Error", "Failed to register patient. Please check the logs."));
            }
        }).start();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) patientRegistrationAnchorPane.getScene().getWindow();
        stage.close();
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
        addressArea.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
