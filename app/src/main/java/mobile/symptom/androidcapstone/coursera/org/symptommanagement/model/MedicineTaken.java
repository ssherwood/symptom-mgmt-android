package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import org.joda.time.LocalDateTime;

/**
 */
public class MedicineTaken {
    private String medicine;
    private LocalDateTime takenAt;

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }
}
