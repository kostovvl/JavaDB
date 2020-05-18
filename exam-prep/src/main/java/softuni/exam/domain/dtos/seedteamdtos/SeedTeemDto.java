package softuni.exam.domain.dtos.seedteamdtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import softuni.exam.domain.dtos.seedpicturedtos.SeedPictureDto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedTeemDto {

    @XmlElement(name = "name")
    @Expose
    private String name;
    @XmlElement(name = "picture")
    @Expose
    private SeedPictureDto picture;

    public SeedTeemDto() {
    }

    @NotNull
    @Length(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NotNull

    public SeedPictureDto getPicture() {
        return picture;
    }

    public void setPicture(SeedPictureDto picture) {
        this.picture = picture;
    }
}
