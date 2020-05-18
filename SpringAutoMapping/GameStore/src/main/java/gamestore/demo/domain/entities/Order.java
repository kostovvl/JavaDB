package gamestore.demo.domain.entities;

import gamestore.demo.domain.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private User buyer;
    private Set<Game> games;

    public Order() {
    }

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

   @ManyToMany(mappedBy = "orders", targetEntity = Game.class,
   fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
