package productshop.domain.dtos;

import productshop.domain.entities.User;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductInRangeDto {

    @Expose
    private String name;

    @Expose
    private BigDecimal price;

    @Expose
    private SellerDto sellerDto;

    public ProductInRangeDto() {
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

    public SellerDto getSellerDto() {
        return sellerDto;
    }

    public void setSellerDto(SellerDto seller) {
        this.sellerDto = seller;
    }
}
