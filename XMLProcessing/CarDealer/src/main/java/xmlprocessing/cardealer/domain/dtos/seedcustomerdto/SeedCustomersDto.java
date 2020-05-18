package xmlprocessing.cardealer.domain.dtos.seedcustomerdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedCustomersDto {

    @XmlElement(name = "customer")
    private List<SeedCustomerDto> customerDtos;

    public SeedCustomersDto() {
    }

    public List<SeedCustomerDto> getCustomerDtos() {
        return customerDtos;
    }

    public void setCustomerDtos(List<SeedCustomerDto> customerDtos) {
        this.customerDtos = customerDtos;
    }
}
