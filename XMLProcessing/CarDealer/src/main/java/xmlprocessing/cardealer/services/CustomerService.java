package xmlprocessing.cardealer.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface CustomerService {

    void seedCustomers() throws JAXBException, FileNotFoundException;

    void writeOrderedCustomers() throws JAXBException;

    void writeTotalSalesByCustomer() throws JAXBException;
}
