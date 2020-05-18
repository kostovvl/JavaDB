package productshop.services;

import java.io.IOException;

public interface ProductService {

    void seedProducts() throws IOException;

    void getProductsInRange(String low, String high) throws IOException;
}
