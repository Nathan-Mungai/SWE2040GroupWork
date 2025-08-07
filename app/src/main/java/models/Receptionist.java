package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receptionist {
    
    private final LongProperty receptionistId = new SimpleLongProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    
    // Use @JsonProperty to map the JSON key 'doctorId' directly to the assignedDoctor property
    @JsonProperty("doctorId")
    private final ObjectProperty<Doctor> assignedDoctor = new SimpleObjectProperty<>();
    
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final ObjectProperty<Admin> createdByAdmin = new SimpleObjectProperty<>();

    public Receptionist() {
        this.firstName.addListener((obs, oldVal, newVal) -> updateFullName());
        this.lastName.addListener((obs, oldVal, newVal) -> updateFullName());
    }

    private void updateFullName() {
        String fName = firstName.get() != null ? firstName.get() : "";
        String lName = lastName.get() != null ? lastName.get() : "";
        fullName.set((fName + " " + lName).trim());
    }

    // Getters for properties
    public LongProperty receptionistIdProperty() { return receptionistId; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty emailProperty() { return email; }
    public StringProperty passwordProperty() { return password; }
    public ObjectProperty<Doctor> assignedDoctorProperty() { return assignedDoctor; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<Admin> createdByAdminProperty() { return createdByAdmin; }

    // Getters
    public Long getReceptionistId() { return receptionistId.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }
    public String getFullName() { return fullName.get(); }
    public String getEmail() { return email.get(); }
    public String getPassword() { return password.get(); }
    public Doctor getAssignedDoctor() { return assignedDoctor.get(); }
    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public Admin getCreatedByAdmin() { return createdByAdmin.get(); }

    // Setters
    public void setReceptionistId(Long receptionistId) { this.receptionistId.set(receptionistId); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPassword(String password) { this.password.set(password); }
    public void setAssignedDoctor(Doctor assignedDoctor) { this.assignedDoctor.set(assignedDoctor); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }
    public void setCreatedByAdmin(Admin createdByAdmin) { this.createdByAdmin.set(createdByAdmin); }
}
