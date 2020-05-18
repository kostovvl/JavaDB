package xmlprocessing.productshop.domain.dtos.userandproductsdto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProductsDtoo {

    @XmlAttribute(name = "count")
    private int count;

    @XmlElement(name = "product")
    private List<SoldProductDtoo> soldProducts;

    public SoldProductsDtoo() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SoldProductDtoo> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<SoldProductDtoo> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
