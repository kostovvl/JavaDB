package alararestaurant.domain.dtos.seedorderdtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedXmlItemDto {

    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "quantity")
    private int quantity;

    public SeedXmlItemDto() {
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
