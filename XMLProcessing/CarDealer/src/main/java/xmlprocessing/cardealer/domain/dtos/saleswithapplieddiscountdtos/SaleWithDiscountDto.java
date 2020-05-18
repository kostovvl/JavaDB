package xmlprocessing.cardealer.domain.dtos.saleswithapplieddiscountdtos;

import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.CarDto;
import xmlprocessing.cardealer.services.CarService;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "sale")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleWithDiscountDto {

    @XmlElement(name = "car")
   private CarSaleDto carDto;
    @XmlElement(name = "customer-name")
    private String customer;
    @XmlElement(name = "discount")
    private double discount;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "price-with-discount")
    private BigDecimal priceWithDiscount;

    public SaleWithDiscountDto() {
    }

    public CarSaleDto getCarDto() {
        return carDto;
    }

    public void setCarDto(CarSaleDto carDto) {
        this.carDto = carDto;
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
}
