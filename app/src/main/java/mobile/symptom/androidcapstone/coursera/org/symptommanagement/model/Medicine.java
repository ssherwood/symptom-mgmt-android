package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import android.os.Parcelable;

/**
 */
public class Medicine extends SDREmbeddedLinks implements Comparable<Medicine> {

    private Long id;
    private String genericName;
    private String brandName;
    private Integer csaSchedule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getCsaSchedule() {
        return csaSchedule;
    }

    public void setCsaSchedule(Integer csaSchedule) {
        this.csaSchedule = csaSchedule;
    }

    @Override
    public int compareTo(Medicine another) {
        return genericName.compareTo(another.getGenericName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Medicine medicine = (Medicine) o;

        if (id != null ? !id.equals(medicine.id) : medicine.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (genericName != null ? genericName.hashCode() : 0);
        result = 31 * result + (brandName != null ? brandName.hashCode() : 0);
        result = 31 * result + (csaSchedule != null ? csaSchedule.hashCode() : 0);
        return result;
    }
}