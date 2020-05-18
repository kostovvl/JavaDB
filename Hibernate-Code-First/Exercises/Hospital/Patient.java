package entities.Hospital;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private Date dateOfBirth;
    private String picture;
    private boolean hasMedicalEnsurance;
    private Set<Visitaion> visitaions;
    private Set<Diagnose> diagnoses;
    private Set<Medicament> medicaments;

    public Patient() {
    }

    @Column(name = "first_name", length = 50, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 50, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "address", length = 50, nullable = false)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "eMail", length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "date_of_birth", nullable = false)
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "picture", columnDefinition = "blob")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column(name = "has_medical_ensurance", nullable = false)
    public boolean isHasMedicalEnsurance() {
        return hasMedicalEnsurance;
    }

    public void setHasMedicalEnsurance(boolean hasMedicalEnsurance) {
        this.hasMedicalEnsurance = hasMedicalEnsurance;
    }

    @OneToMany(mappedBy = "patient", targetEntity = Visitaion.class,
    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Visitaion> getVisitaions() {
        return visitaions;
    }

    public void setVisitaions(Set<Visitaion> visitaions) {
        this.visitaions = visitaions;
    }

    @ManyToMany(mappedBy = "patients", targetEntity = Diagnose.class,
    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }

    @ManyToMany(mappedBy = "patients", targetEntity = Medicament.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(Set<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}
