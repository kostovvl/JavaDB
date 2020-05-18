package xmlprocessing.productshop.domain.dtos.seedproductdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedProductsDto {

    @XmlElement(name = "product")
    private List<SeedProductDto> products;

    public SeedProductsDto() {
    }

    public List<SeedProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<SeedProductDto> products) {
        this.products = products;
    }
}
