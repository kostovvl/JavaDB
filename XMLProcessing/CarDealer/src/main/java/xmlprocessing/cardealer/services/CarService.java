package xmlprocessing.cardealer.services;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface CarService {

    void seedCars() throws JAXBException, FileNotFoundException;

    void writeCarsFromMakeToyota() throws JAXBException;

    void writeCarsWithTheirParts() throws JAXBException;


}
