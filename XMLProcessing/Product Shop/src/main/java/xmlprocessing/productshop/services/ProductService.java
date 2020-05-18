package xmlprocessing.productshop.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface ProductService {

    void seedProducts() throws JAXBException, FileNotFoundException;

    void writeProductsInRange() throws JAXBException;
}
