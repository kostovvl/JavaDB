package xmlprocessing.cardealer.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface PartService {

    void seedParts() throws JAXBException, FileNotFoundException;
}
