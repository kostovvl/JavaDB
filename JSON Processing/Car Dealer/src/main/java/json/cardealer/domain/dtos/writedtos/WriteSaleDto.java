package json.cardealer.domain.dtos.writedtos;

import com.google.gson.annotations.Expose;
import json.cardealer.domain.entites.Car;
import json.cardealer.domain.entites.Customer;
import json.cardealer.domain.entites.Part;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Set;

public class WriteSaleDto {

    @Expose
    private WriteCarDto car;

    @Expose
    private String customer;

    @Expose
    private double discount;

    @Expose
    private BigDecimal price;

    @Expose
    private BigDecimal priceWithDiscount;


    public WriteSaleDto() {
    }

    public WriteCarDto getCar() {
        return car;
    }

    public void setCar(WriteCarDto car) {
        this.car = car;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }

    public void calculatePrice() {
        BigDecimal result = new BigDecimal("0");
        for (Part part : this.car.getParts()) {
            result = result.add(part.getPrice());
        }
        this.price = result;
    }

    public void calculatePriceWithDiscount() {
        this.priceWithDiscount =
             this.price.subtract(this.price.multiply(new BigDecimal(discount / 100)))
                     .round(new MathContext(5));
    }
}
