package json.cardealer.domain.entites;

import json.cardealer.domain.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity {

    private String name;
    private boolean isImporter;
    private Set<Part> pars;

    public Supplier() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "is_importer")
    public boolean isImporter() {
        return isImporter;
    }

    public void setImporter(boolean importer) {
        isImporter = importer;
    }

    @OneToMany(mappedBy = "supplier", targetEntity = Part.class,
    fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Part> getPars() {
        return pars;
    }

    public void setPars(Set<Part> pars) {
        this.pars = pars;
    }
}
