package models;

// This DTO is for the client-side to match the server's API expectation
public class QueueRequestDto {
    private Long patientId;
    private Long doctorId;
    private Long receptionistId;

    public QueueRequestDto(Long patientId, Long doctorId, Long receptionistId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.receptionistId = receptionistId;
    }

    // Getters and Setters
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(Long receptionistId) {
        this.receptionistId = receptionistId;
    }
}
