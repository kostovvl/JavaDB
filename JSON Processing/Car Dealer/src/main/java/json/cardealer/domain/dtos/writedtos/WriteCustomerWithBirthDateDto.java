package json.cardealer.domain.dtos.writedtos;

import com.google.gson.annotations.Expose;
import json.cardealer.domain.entites.Sale;

import java.time.LocalDate;
import java.util.Set;

public class WriteCustomerWithBirthDateDto {

    @Expose
    private long id;

    @Expose
    private String name;

    @Expose
    private String dateOfBirth;

    @Expose
    private boolean isYoungDriver;

    @Expose
    private Set<Sale> saless;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WriteCustomerWithBirthDateDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public Set<Sale> getSaless() {
        return saless;
    }

    public void setSaless(Set<Sale> saless) {
        this.saless = saless;
    }
}
