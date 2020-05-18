package json.cardealer.services;

import java.io.IOException;

public interface SupplierService {

    void seedSuppliers() throws IOException;

    void writeLocalSuppliers() throws IOException;
}
