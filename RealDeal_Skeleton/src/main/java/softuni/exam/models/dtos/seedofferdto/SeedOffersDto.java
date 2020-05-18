package softuni.exam.models.dtos.seedofferdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedOffersDto {

    @XmlElement(name = "offer")
    private List<SeedOfferDto> offers;

    public SeedOffersDto() {
    }

    public List<SeedOfferDto> getOffers() {
        return offers;
    }

    public void setOffers(List<SeedOfferDto> offers) {
        this.offers = offers;
    }
}
