package json.cardealer.domain.entites;

import json.cardealer.domain.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "parts")
public class Part extends BaseEntity {

    private String name;
    private BigDecimal price;
    private int quantity;
    private Set<Car> cars;
    private Supplier supplier;

    public Part() {
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @ManyToMany(mappedBy = "parts", targetEntity = Car.class,
    fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @ManyToOne()
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
