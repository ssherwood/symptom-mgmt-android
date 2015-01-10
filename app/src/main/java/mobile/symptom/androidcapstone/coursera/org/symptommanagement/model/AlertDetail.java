package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import org.joda.time.LocalDateTime;

/**
 */
public class AlertDetail extends SDREmbeddedLinks {
    private Long id;
    private LocalDateTime alertedAt;
    private String alertType;
    private LocalDateTime acknowledgedAt;
    private String notes;
    private Patient patient;

    AlertDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAlertedAt() {
        return alertedAt;
    }

    public void setAlertedAt(LocalDateTime alertedAt) {
        this.alertedAt = alertedAt;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public LocalDateTime getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) {
        this.acknowledgedAt = acknowledgedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
