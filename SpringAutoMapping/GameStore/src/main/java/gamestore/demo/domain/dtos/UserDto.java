package gamestore.demo.domain.dtos;

import gamestore.demo.domain.entities.Game;
import gamestore.demo.domain.entities.Order;
import gamestore.demo.domain.entities.Status;

import java.util.Set;

public class UserDto {

    private String email;
    private String password;
    private String fullName;
    private Status status;
    private Set<Game> games;
    private Set<Order> orders;

    public UserDto() {
    }

    public UserDto(String email, String password, String fullName, Status status, Set<Game> games, Set<Order> orders) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.status = status;
        this.games = games;
        this.orders = orders;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
