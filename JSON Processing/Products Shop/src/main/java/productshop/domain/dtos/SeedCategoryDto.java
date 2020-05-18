package productshop.domain.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class SeedCategoryDto {

    @Expose
    private String name;

    public SeedCategoryDto() {
    }

    @Length(min = 5, max = 15, message = "Category name must be between 5 and 15 characters!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
