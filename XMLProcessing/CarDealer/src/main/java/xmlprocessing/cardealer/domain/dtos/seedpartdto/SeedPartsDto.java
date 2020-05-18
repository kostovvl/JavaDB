package xmlprocessing.cardealer.domain.dtos.seedpartdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "parts")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedPartsDto {

    @XmlElement(name = "part")
    private List<SeedPartDto> partDtos;

    public SeedPartsDto() {
    }

    public List<SeedPartDto> getPartDtos() {
        return partDtos;
    }

    public void setPartDtos(List<SeedPartDto> partDtos) {
        this.partDtos = partDtos;
    }
}
