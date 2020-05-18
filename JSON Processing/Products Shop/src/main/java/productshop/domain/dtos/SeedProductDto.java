package productshop.domain.dtos;

import productshop.domain.entities.User;
import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class SeedProductDto {

    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private User buyer;

    @Expose
    private User seller;

    public SeedProductDto() {
    }

    @Length(min = 3, message = "Product name must be at least 3 characters long")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
