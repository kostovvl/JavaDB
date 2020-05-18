package softuni.exam.domain.dtos.seedplayerdtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import softuni.exam.domain.dtos.seedpicturedtos.SeedPictureDto;
import softuni.exam.domain.dtos.seedteamdtos.SeedTeemDto;
import softuni.exam.domain.entities.Position;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SeedPlayerDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int number;
    @Expose
    private BigDecimal salary;
    @Expose
    private Position position;
    @Expose
    private SeedPictureDto picture;
    @Expose
    private SeedTeemDto team;

    public SeedPlayerDto() {
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    @Length(min = 3, max = 15)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull
    @Min(value = 0)
    @Max(value = 99)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NotNull
    @Min(value = 0)
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @NotNull
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @NotNull
    public SeedPictureDto getPicture() {
        return picture;
    }

    public void setPicture(SeedPictureDto picture) {
        this.picture = picture;
    }

    @NotNull
    public SeedTeemDto getTeam() {
        return team;
    }

    public void setTeam(SeedTeemDto team) {
        this.team = team;
    }
}
