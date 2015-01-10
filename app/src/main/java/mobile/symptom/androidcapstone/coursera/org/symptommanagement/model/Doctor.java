package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

/**
 */
public class Doctor extends SDREmbeddedLinks {
    private String username;
    private Integer nationalProviderNumber;
    private String firstName;
    private String lastName;

    protected Doctor() {
    }

    public Doctor(String username, Integer nationalProviderNumber, String firstName, String lastName) {
        this.username = username;
        this.nationalProviderNumber = nationalProviderNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNationalProviderNumber() {
        return nationalProviderNumber;
    }

    public void setNationalProviderNumber(Integer nationalProviderNumber) {
        this.nationalProviderNumber = nationalProviderNumber;
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
}
