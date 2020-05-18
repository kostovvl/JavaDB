package xmlprocessing.productshop.domain.dtos.seedproductdto;


import org.hibernate.validator.constraints.Length;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedProductDto {

    @XmlElement(name = "name")
    @Length(min = 3, message = "Product name must be at least 3 characters long!")
    private String name;
    @XmlElement(name = "price")
    private BigDecimal price;

    public SeedProductDto() {
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

    public void setPrice(String price) {
        this.price = new BigDecimal(price);
    }
}
