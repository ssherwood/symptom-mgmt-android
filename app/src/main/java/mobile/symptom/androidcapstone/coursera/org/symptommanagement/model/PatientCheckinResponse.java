package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import java.util.List;

/**
 */
public class PatientCheckinResponse {
    private List<PatientCheckin> patientCheckins;

    public List<PatientCheckin> getPatientCheckins() {
        return patientCheckins;
    }

    public void setPatientCheckins(List<PatientCheckin> patientCheckins) {
        this.patientCheckins = patientCheckins;
    }
}
