package xmlprocessing.cardealer.domain.dtos.seedsuppliersdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedSuppliersDto {

    @XmlElement(name = "supplier")
    private List<SeedSupplierDto> suppliers;

    public SeedSuppliersDto() {
    }

    public List<SeedSupplierDto> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<SeedSupplierDto> suppliers) {
        this.suppliers = suppliers;
    }
}
