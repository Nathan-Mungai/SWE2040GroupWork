// QueuedPatient.java
package models;

import java.time.LocalDateTime;
import java.time.Period;

/**
 * A wrapper class to combine Patient and Queue data for display in the UI.
 * This class provides custom getters that match the property names used in the TableView.
 */
public class QueuedPatient {
    private Patient patient;
    private Queue queue;

    /**
     * Constructs a QueuedPatient object.
     *
     * @param patient The Patient object.
     * @param queue   The Queue object, can be null if the patient is not in a queue.
     */
    public QueuedPatient(Patient patient, Queue queue) {
        this.patient = patient;
        this.queue = queue;
    }

    // Custom getters for the TableView.
    // These methods provide a single point of access for complex properties.

    /**
     * Gets the patient's ID.
     * @return The patient's ID.
     */
    public Long getPatientId() {
        return patient.getPatientId();
    }

    /**
     * Gets the full name of the patient.
     * @return The patient's full name.
     */
    public String getFullName() {
        return patient.getFirstName() + " " + patient.getLastName();
    }

    /**
     * Gets the patient's gender.
     * @return The patient's gender.
     */
    public String getGender() {
        return patient.getGender();
    }

    /**
     * Calculates and gets the patient's age based on their date of birth.
     * @return The patient's age in years.
     */
    public Integer getAge() {
        if (patient.getDateOfBirth() != null) {
            return Period.between(patient.getDateOfBirth(), java.time.LocalDate.now()).getYears();
        }
        return 0;
    }

    /**
     * Gets the patient's phone number.
     * @return The patient's phone number.
     */
    public String getPhoneNumber() {
        return patient.getPhoneNumber();
    }

    /**
     * Gets the patient's current status in the queue.
     * @return "Waiting" or "Not in Queue".
     */
    public String getPatientStatus() {
        return queue != null ? queue.getStatus() : "Not in Queue";
    }
    
    /**
     * Gets the queue ID for this patient.
     * @return The queue ID, or null if the patient is not in a queue.
     */
    public Long getQueueId() {
        return queue != null ? queue.getQueueId() : null;
    }

    /**
     * Gets the timestamp when the patient was added to the queue.
     * @return The queued timestamp, or null if the patient is not in a queue.
     */
    public LocalDateTime getQueuedAt() {
        return queue != null ? queue.getQueuedAt() : null;
    }

    // Getters for the underlying objects (useful if you need to access other properties)
    public Patient getPatient() {
        return patient;
    }

    public Queue getQueue() {
        return queue;
    }
}
