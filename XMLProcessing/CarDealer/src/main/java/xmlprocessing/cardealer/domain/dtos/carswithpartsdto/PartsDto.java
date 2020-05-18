package xmlprocessing.cardealer.domain.dtos.carswithpartsdto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "parts")
@XmlAccessorType(XmlAccessType.FIELD)
public class PartsDto {

    @XmlElement(name = "part")
    private List<PartDto> parts;

    public PartsDto() {
    }

    public List<PartDto> getParts() {
        return parts;
    }

    public void setParts(List<PartDto> parts) {
        this.parts = parts;
    }
}
