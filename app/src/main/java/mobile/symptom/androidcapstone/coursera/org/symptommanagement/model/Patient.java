package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

/**
 */
public class Patient extends SDREmbeddedLinks implements Parcelable {

    private Long id;
    private String medicalId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private LocalDate dateOfBirth;

    Patient() {
    }

    public Patient(Parcel in) {
        id = in.readLong();
        medicalId = in.readString();
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        emailAddress = in.readString();
        String dateString = in.readString();
        dateOfBirth = "".equals(dateString) ? null : LocalDate.parse(dateString);
    }

    ///

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    ///
    public static final Parcelable.Creator<Patient> CREATOR = new Parcelable.Creator<Patient>() {
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(medicalId);
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(emailAddress);
        dest.writeString(dateOfBirth == null ? "" : dateOfBirth.toString("yyyy-MM-dd"));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}