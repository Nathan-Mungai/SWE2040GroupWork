package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Doctor;
import models.Receptionist;
import services.DoctorService;
import services.ReceptionistService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginViewController {

    private static final Logger LOGGER = Logger.getLogger(LoginViewController.class.getName());

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final DoctorService doctorService = new DoctorService();
    private final ReceptionistService receptionistService = new ReceptionistService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter your email and password.");
            return;
        }

        // Try to login as a doctor
        try {
            Doctor doctor = doctorService.getDoctorByEmail(email);
            if (doctor != null) {
                // Assuming password check is handled by the backend or is not required for this demo
                // TODO: Implement actual password validation if needed
                
                showDoctorDashboard(doctor);
                return;
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Login as doctor failed: " + e.getMessage());
        }

        // If doctor login failed, try to login as a receptionist
        try {
            Receptionist receptionist = receptionistService.getReceptionistByEmail(email);
            if (receptionist != null) {
                // Assuming password check is handled by the backend or is not required for this demo
                // TODO: Implement actual password validation if needed
                
                showReceptionistDashboard(receptionist);
                return;
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Login as receptionist failed: " + e.getMessage());
        }

        // If both failed
        showAlert("Error", "Invalid email or password. Please try again.");
    }

    private void showDoctorDashboard(Doctor doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/doctorsplaza/fxml/DoctorDashboard.fxml"));
            Parent root = loader.load();
            DoctorDashboardController controller = loader.getController();
            controller.setDoctor(doctor);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor Dashboard");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Doctor Dashboard", e);
            showAlert("Error", "Failed to load the doctor dashboard. Please contact support.");
        }
    }

    private void showReceptionistDashboard(Receptionist receptionist) {
        try {
            // The Receptionist object now comes from the service call with the assignedDoctor
            // property already populated due to the @JsonProperty annotation in the model.
            // No need to fetch the doctor separately or set it here.
            if (receptionist.getAssignedDoctor() == null) {
                showAlert("Error", "No doctor found for this receptionist.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/doctorsplaza/fxml/ReceptionistDashboard.fxml"));
            Parent root = loader.load();
            ReceptionistDashboardController controller = loader.getController();
            // Pass the complete receptionist object to the dashboard controller
            controller.setReceptionist(receptionist);
            
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Receptionist Dashboard");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Receptionist Dashboard", e);
            showAlert("Error", "Failed to load the receptionist dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
