package json.cardealer.domain.dtos.writedtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class WriteCustomerDto {

    @Expose
    private String name;

    @Expose
    private int boughtCars;

    @Expose
    private BigDecimal totalMoneySpent;

    public WriteCustomerDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBoughtCars() {
        return boughtCars;
    }

    public void setBoughtCars(int boughtCars) {
        this.boughtCars = boughtCars;
    }

    public BigDecimal getTotalMoneySpent() {
        return totalMoneySpent;
    }

    public void setTotalMoneySpent(BigDecimal totalMoneySpent) {
        this.totalMoneySpent = totalMoneySpent;
    }
}
