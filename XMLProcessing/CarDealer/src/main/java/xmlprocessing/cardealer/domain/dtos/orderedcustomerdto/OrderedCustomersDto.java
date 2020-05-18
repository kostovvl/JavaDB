package xmlprocessing.cardealer.domain.dtos.orderedcustomerdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderedCustomersDto {

    @XmlElement(name = "customer")
    private List<OrderedCustomerDto> customers;

    public OrderedCustomersDto() {
    }

    public List<OrderedCustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<OrderedCustomerDto> customers) {
        this.customers = customers;
    }
}
