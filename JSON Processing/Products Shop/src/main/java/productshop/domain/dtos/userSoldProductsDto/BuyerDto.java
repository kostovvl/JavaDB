package productshop.domain.dtos.userSoldProductsDto;

import com.google.gson.annotations.Expose;

public class BuyerDto {
    @Expose
    private String firstName;

    @Expose
    private String lastName;

    public BuyerDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
