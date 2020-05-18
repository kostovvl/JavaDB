package xmlprocessing.cardealer.domain.dtos.customerswithsalesdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWithSalesDto {

    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "bought-cars")
    private int boughtCars;
    @XmlAttribute(name = "spent-money")
    private BigDecimal totalMoneySpent;

    public CustomerWithSalesDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBoughtCars() {
        return boughtCars;
    }

    public void setBoughtCars(int boughtCars) {
        this.boughtCars = boughtCars;
    }

    public BigDecimal getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public void setTotalMoneySpent(BigDecimal totalMoneySpent) {
        this.totalMoneySpent = totalMoneySpent;
    }
}
