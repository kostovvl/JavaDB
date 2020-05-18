package hiberspring.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String name;
    private long population;
    private Branch branch;

    public Town() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "population")
    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @OneToOne(mappedBy = "town", targetEntity = Branch.class,
    fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
