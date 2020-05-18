package softuni.exam.models.dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedSellersDto {

    @XmlElement(name = "seller")
    private List<SeedSellerDto> sellers;

    public SeedSellersDto() {
    }

    public List<SeedSellerDto> getSellers() {
        return sellers;
    }

    public void setSellers(List<SeedSellerDto> sellers) {
        this.sellers = sellers;
    }
}
