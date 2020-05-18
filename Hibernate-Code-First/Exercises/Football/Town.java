package entities.football;

import entities.BaseEntity;

import javax.persistence.*;
import javax.xml.namespace.QName;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    private String name;
    private Country country;
    private Set<Team> teams;

    public Town() {
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    public Country getCountry() {

        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @OneToMany
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }


}
