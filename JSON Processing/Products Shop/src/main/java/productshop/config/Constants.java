package productshop.config;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static final String USERS_URL = "src/main/resources/files/importJsons/users.json";
    public static final String PRODUCTS_URL = "src/main/resources/files/importJsons/products.json";
    public static final String CATEGORIES_URL = "src/main/resources/files/importJsons/categories.json";

    public static final String PRODUCTS_IN_RANGE_URL
            = "src/main/resources/files/exportJsons/products-in-range.json";

    public static final String USER_SOLD_PRODUCTS_URL
            = "src/main/resources/files/exportJsons/users-sold-products.json";

    public static final String CATEGORIES__URL
            = "src/main/resources/files/exportJsons/categories-by-products.json";

    public static final String USERS_AND_PRODUCTS_URL
            = "src/main/resources/files/exportJsons/users-and-products.json";
}
