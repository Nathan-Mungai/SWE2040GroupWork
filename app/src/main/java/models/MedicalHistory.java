package models;

public class MedicalHistory {
    private Long historyId;
    private Patient patientId;
    private String familyHistory;
    private String medications;
    private String preExistingConditions;
    private String allergies;

    public Long getHistoryId() { return historyId; }
    public void setHistoryId(Long historyId) { this.historyId = historyId; }
    public Patient getPatientId() { return patientId; }
    public void setPatientId(Patient patientId) { this.patientId = patientId; }
    public String getFamilyHistory() { return familyHistory; }
    public void setFamilyHistory(String familyHistory) { this.familyHistory = familyHistory; }
    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }
    public String getPreExistingConditions() { return preExistingConditions; }
    public void setPreExistingConditions(String preExistingConditions) { this.preExistingConditions = preExistingConditions; }
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
}
