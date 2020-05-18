package xmlprocessing.productshop.domain.dtos.seedcategorydto;

import org.hibernate.validator.constraints.Length;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeedCategoryDto {

    @XmlElement(name = "name")
    @Length(min = 3, max = 15, message = "Category name must be between 3 and 15 characters!")
    private String name;

    public SeedCategoryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
