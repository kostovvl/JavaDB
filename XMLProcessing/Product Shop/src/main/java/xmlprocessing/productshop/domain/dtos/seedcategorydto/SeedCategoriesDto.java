package xmlprocessing.productshop.domain.dtos.seedcategorydto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedCategoriesDto {

    @XmlElement(name = "category")
    private List<SeedCategoryDto> seedCategoryDtos;

    public SeedCategoriesDto() {
    }

    public List<SeedCategoryDto> getSeedCategoryDtos() {
        return seedCategoryDtos;
    }

    public void setSeedCategoryDtos(List<SeedCategoryDto> seedCategoryDtos) {
        this.seedCategoryDtos = seedCategoryDtos;
    }
}
