package json.cardealer.services;

import java.io.IOException;

public interface SaleService {

    void seedSales();

    void writeSalesWithAppliedDiscount() throws IOException;
}
