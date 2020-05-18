package xmlprocessing.cardealer.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface SupplierService {

    void seedSuppliers() throws JAXBException, FileNotFoundException;

    void writeLocalSuppliers() throws JAXBException;
}
