package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import java.util.List;

/**
 */
public class AlertResponse {
    private List<Alert> patientAlerts;

    public List<Alert> getPatientAlerts() {
        return patientAlerts;
    }

    public void setPatientAlerts(List<Alert> patientAlerts) {
        this.patientAlerts = patientAlerts;
    }
}
