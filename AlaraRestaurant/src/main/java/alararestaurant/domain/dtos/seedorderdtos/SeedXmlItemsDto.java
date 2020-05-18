package alararestaurant.domain.dtos.seedorderdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedXmlItemsDto {

    @XmlElement(name = "item")
    private List<SeedXmlItemDto> items;


    public SeedXmlItemsDto() {
    }


    public List<SeedXmlItemDto> getItems() {
        return items;
    }

    public void setItems(List<SeedXmlItemDto> items) {
        this.items = items;
    }
}
