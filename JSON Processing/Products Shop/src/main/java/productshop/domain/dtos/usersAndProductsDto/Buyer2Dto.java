package productshop.domain.dtos.usersAndProductsDto;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class Buyer2Dto {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private int age;

    @Expose
    private Set<Product2Dto> soldProducts;

    public Buyer2Dto() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<Product2Dto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(Set<Product2Dto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
