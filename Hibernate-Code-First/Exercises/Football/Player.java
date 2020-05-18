package entities.football;

import entities.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "players")
public class Player extends BaseEntity {

    private String name;
    private int squatNumber;
    private Team team;
    private Position position;
    private boolean isCurrentlyInjured;
    private Set<Game> games;

    public Player() {
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    public int getSquatNumber() {
        return squatNumber;
    }

    public void setSquatNumber(int squatNumber) {
        this.squatNumber = squatNumber;
    }

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @ManyToOne
    @JoinColumn(name = "possition_id", referencedColumnName = "id")
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Column(name = "is_currently_injured")
    public boolean isCurrentlyInjured() {
        return isCurrentlyInjured;
    }

    public void setCurrentlyInjured(boolean currentlyInjured) {

        isCurrentlyInjured = currentlyInjured;
    }

    @ManyToMany(mappedBy = "players", targetEntity = Game.class,
    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
