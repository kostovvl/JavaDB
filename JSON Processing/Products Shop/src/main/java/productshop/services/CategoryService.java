package productshop.services;

import java.io.IOException;

public interface CategoryService {

    void seedCategories() throws IOException;

    void findAllCategories() throws IOException;
}
