package json.cardealer.services;

import java.io.IOException;

public interface CustomerService {

    void seedCustomers() throws IOException;

    void writeAllWithTotalSales() throws IOException;

    void writeOrderedCustomers() throws IOException;
}
