package xmlprocessing.cardealer.domain.dtos.carsfromtoyotadto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarsFromToyotaDto {

    @XmlElement(name = "car")
    private List<CarFromToyotaDto> cars;

    public CarsFromToyotaDto() {
    }

    public List<CarFromToyotaDto> getCars() {
        return cars;
    }

    public void setCars(List<CarFromToyotaDto> cars) {
        this.cars = cars;
    }
}
