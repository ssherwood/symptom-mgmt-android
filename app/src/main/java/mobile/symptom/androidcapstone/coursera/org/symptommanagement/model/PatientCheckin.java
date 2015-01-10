package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class PatientCheckin extends SDREmbeddedLinks implements Parcelable {

    public static final String PAIN_STATUS_CONTROLLED = "CONTROLLED";
    public static final String PAIN_STATUS_MODERATE = "MODERATE";
    public static final String PAIN_STATUS_SEVERE = "SEVERE";

    public static final String EATING_STATUS_YES = "EATING";
    public static final String EATING_STATUS_SOME = "LIMITED";
    public static final String EATING_STATUS_NO = "NOT_EATING";

    private String patient; // href
    private LocalDateTime checkinAt;
    private String painStatus;
    private String eatingStatus;
    private List<String> medicineTakenRefs;

    public PatientCheckin() {
        medicineTakenRefs = new ArrayList<>();
    }

    ///

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public LocalDateTime getCheckinAt() {
        return checkinAt;
    }

    public void setCheckinAt(LocalDateTime checkinAt) {
        this.checkinAt = checkinAt;
    }

    public String getPainStatus() {
        return painStatus;
    }

    public void setPainStatus(String painStatus) {
        this.painStatus = painStatus;
    }

    public String getEatingStatus() {
        return eatingStatus;
    }

    public void setEatingStatus(String eatingStatus) {
        this.eatingStatus = eatingStatus;
    }

    public List<String> getMedicineTakenRefs() {
        return medicineTakenRefs;
    }

    public void setMedicineTakenRefs(List<String> medicineTakenRefs) {
        this.medicineTakenRefs = medicineTakenRefs;
    }

    public void addMedicineTakenRef(String medicineTakenRef) {
        this.medicineTakenRefs.add(medicineTakenRef);
    }

    ///

    @Override
    public String toString() {
        return "On " + getCheckinAt().toString("MMMM dd") +
               " at " + getCheckinAt().toString("hh:mm a") +
               ", you last reported your pain as being '" + getPainReadableStatus() + "' and were " + getEatingReadableStatus() + ".";
    }

    public String getPainReadableStatus() {
        if (PAIN_STATUS_CONTROLLED.equals(painStatus)) {
            return "well-controlled";
        }
        else if (PAIN_STATUS_MODERATE.equals(painStatus)) {
            return "moderate";
        }
        else if (PAIN_STATUS_SEVERE.equals(painStatus)) {
            return "severe";
        }

        return "unknown";
    }

    public String getEatingReadableStatus() {
        if (EATING_STATUS_YES.equals(eatingStatus)) {
            return "eating normally";
        }
        else if (EATING_STATUS_SOME.equals(eatingStatus)) {
            return "able to eat some";
        }
        else if (EATING_STATUS_NO.equals(eatingStatus)) {
            return "NOT able to eat";
        }

        return "unknown";
    }

    ///

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.patient);
        dest.writeString(checkinAt == null ? "" : checkinAt.toString("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        dest.writeString(this.painStatus);
        dest.writeString(this.eatingStatus);
        dest.writeStringList(this.medicineTakenRefs);
    }

    private PatientCheckin(Parcel in) {
        this.patient = in.readString();
        String checkinAtString = in.readString();
        checkinAt = "".equals(checkinAtString) ? null : LocalDateTime.parse(checkinAtString);
        this.painStatus = in.readString();
        this.eatingStatus = in.readString();
        this.medicineTakenRefs = new ArrayList<>();
        in.readStringList(this.medicineTakenRefs);
    }

    public static final Creator<PatientCheckin> CREATOR = new Creator<PatientCheckin>() {
        public PatientCheckin createFromParcel(Parcel source) {
            return new PatientCheckin(source);
        }

        public PatientCheckin[] newArray(int size) {
            return new PatientCheckin[size];
        }
    };
}