package xmlprocessing.cardealer.domain.dtos.seedcardto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedCarsDto {

    @XmlElement(name = "car")
    private List<SeedCarDto> carDtos;

    public SeedCarsDto() {
    }

    public List<SeedCarDto> getCarDtos() {
        return carDtos;
    }

    public void setCarDtos(List<SeedCarDto> carDtos) {
        this.carDtos = carDtos;
    }
}
