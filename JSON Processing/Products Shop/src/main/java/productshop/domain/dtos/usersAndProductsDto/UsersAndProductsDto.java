package productshop.domain.dtos.usersAndProductsDto;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UsersAndProductsDto {

    @Expose
    private int count;

    @Expose
    private Set<Buyer2Dto> users;

    public UsersAndProductsDto() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<Buyer2Dto> getUsers() {
        return users;
    }

    public void setUsers(Set<Buyer2Dto> buyers) {
        this.users = buyers;
    }
}
