package alararestaurant.domain.dtos.seedorderdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedOrdersDtos {

    @XmlElement(name = "order")
    private List<SeedOrderDto> orders;

    public SeedOrdersDtos() {
    }

    public List<SeedOrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<SeedOrderDto> orders) {
        this.orders = orders;
    }
}
