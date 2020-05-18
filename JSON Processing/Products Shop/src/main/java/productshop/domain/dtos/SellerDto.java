package productshop.domain.dtos;

import com.google.gson.annotations.Expose;

public class SellerDto {

    @Expose
    private String name;

    public SellerDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
