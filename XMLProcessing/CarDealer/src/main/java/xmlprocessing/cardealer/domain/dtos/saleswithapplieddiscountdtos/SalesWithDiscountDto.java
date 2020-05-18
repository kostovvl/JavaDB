package xmlprocessing.cardealer.domain.dtos.saleswithapplieddiscountdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalesWithDiscountDto {

    @XmlElement(name = "sale")
    private List<SaleWithDiscountDto> sales;

    public SalesWithDiscountDto() {
    }

    public List<SaleWithDiscountDto> getSales() {
        return sales;
    }

    public void setSales(List<SaleWithDiscountDto> sales) {
        this.sales = sales;
    }
}
