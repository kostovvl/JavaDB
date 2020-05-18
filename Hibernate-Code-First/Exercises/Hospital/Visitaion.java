package entities.Hospital;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "visitations")
public class Visitaion extends BaseEntity {

    private Date date;
    private String comment;
    private Patient patient;

    public Visitaion() {
    }

    @Column(name = "visitation_date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "comment", length = 200)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
