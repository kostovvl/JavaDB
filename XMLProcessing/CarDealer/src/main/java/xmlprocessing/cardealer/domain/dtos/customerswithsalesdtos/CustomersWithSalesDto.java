package xmlprocessing.cardealer.domain.dtos.customerswithsalesdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomersWithSalesDto {

    @XmlElement(name = "customer")
    private List<CustomerWithSalesDto> customers;

    public CustomersWithSalesDto() {
    }

    public List<CustomerWithSalesDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerWithSalesDto> customers) {
        this.customers = customers;
    }
}
