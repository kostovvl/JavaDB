package productshop.domain.dtos.userSoldProductsDto;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductDto {

    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private BuyerDto buyer;

    public ProductDto() {
    }

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

    public BuyerDto getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerDto buyer) {
        this.buyer = buyer;
    }
}
