package json.cardealer.config;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static final String CARS_FILE_URL = "src/main/resources/Files/importJsons/cars.json";
    public static final String CUSTOMERS_FILE_URL = "src/main/resources/Files/importJsons/customers.json";
    public static final String PARTS_FILE_URL = "src/main/resources/Files/importJsons/parts.json";
    public static final String SUPPLIERS_FILE_URL = "src/main/resources/Files/importJsons/suppliers.json";


    public static final String TOYOTA_CARS_FILE_URL =
            "src/main/resources/Files/exportJsons/toyota-cars.json";
    public static final String LOCAL_SUPPLIERS_FILE_URL =
            "src/main/resources/Files/exportJsons/local-suppliers.json";
    public static final String CARS_AND_PARTS_FILE_URL =
            "src/main/resources/Files/exportJsons/cars-and-parts.json";
    public static final String TOTAL_SALES_BY_CUSTOMER_FILE_URL =
            "src/main/resources/Files/exportJsons/total-sales-by-customer.json";
    public static final String SALES_WITH_DISCOUNT_FILE_URL =
            "src/main/resources/Files/exportJsons/sales-with-discount.json";
    public static final String ORDERED_CUSTOMERS_FILE_URL =
            "src/main/resources/Files/exportJsons/ordered-customers.json";

}
