package productshop.services;

import java.io.IOException;

public interface UserService {

    void seedUsers() throws IOException;

    void getUsersWithSoldItems() throws IOException;

    void getUserWithSoldItemsOrdered() throws IOException;
}
