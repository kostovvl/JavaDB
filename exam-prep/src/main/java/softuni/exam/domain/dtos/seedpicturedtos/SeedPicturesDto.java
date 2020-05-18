package softuni.exam.domain.dtos.seedpicturedtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedPicturesDto {

    @XmlElement(name = "picture")
    private List<SeedPictureDto> pictures;

    public SeedPicturesDto() {
    }

    public List<SeedPictureDto> getPictures() {
        return pictures;
    }

    public void setPictures(List<SeedPictureDto> pictures) {
        this.pictures = pictures;
    }
}
