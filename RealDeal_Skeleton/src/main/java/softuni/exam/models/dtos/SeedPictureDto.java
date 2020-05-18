package softuni.exam.models.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SeedPictureDto {

    @Expose
    private String name;
    @Expose
    private String dateAndTime;
    @Expose
    private long car;

    public SeedPictureDto() {
    }

    @NotNull
    @Length(min = 3, max = 19)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @NotNull
    public long getCar() {
        return car;
    }

    public void setCar(long car) {
        this.car = car;
    }
}
