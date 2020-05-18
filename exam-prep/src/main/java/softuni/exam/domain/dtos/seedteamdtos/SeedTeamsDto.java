package softuni.exam.domain.dtos.seedteamdtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedTeamsDto {

    @XmlElement(name = "team")
    private List<SeedTeemDto>teems;

    public SeedTeamsDto() {
    }

    public List<SeedTeemDto> getTeems() {
        return teems;
    }

    public void setTeems(List<SeedTeemDto> teems) {
        this.teems = teems;
    }
}
