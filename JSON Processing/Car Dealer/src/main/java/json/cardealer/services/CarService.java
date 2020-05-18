package json.cardealer.services;

import java.io.IOException;

public interface CarService {

    void seedCars() throws IOException;

    void writeAllToyotas() throws IOException;

    void writeCarsWithTheirListOfParts() throws IOException;
}
