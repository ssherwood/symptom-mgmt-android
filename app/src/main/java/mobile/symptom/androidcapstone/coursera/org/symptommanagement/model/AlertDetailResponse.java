package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import java.util.List;

/**
 */
public class AlertDetailResponse {
    private List<AlertDetail> patientAlerts;

    public List<AlertDetail> getPatientAlerts() {
        return patientAlerts;
    }

    public void setPatientAlerts(List<AlertDetail> patientAlerts) {
        this.patientAlerts = patientAlerts;
    }
}
