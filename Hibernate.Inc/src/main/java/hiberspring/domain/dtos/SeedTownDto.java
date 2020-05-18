package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SeedTownDto {

    @Expose
    private String name;
    @Expose
    private long population;

    public SeedTownDto() {
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Min(value = 0)
    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }
}
