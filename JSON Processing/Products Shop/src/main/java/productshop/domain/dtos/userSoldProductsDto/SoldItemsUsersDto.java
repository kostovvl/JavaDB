package productshop.domain.dtos.userSoldProductsDto;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class SoldItemsUsersDto {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private Set<ProductDto> selling;

    public SoldItemsUsersDto() {
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

    public Set<ProductDto> getSelling() {
        return selling;
    }

    public void setSelling(Set<ProductDto> selling) {
        this.selling = selling;
    }
}
