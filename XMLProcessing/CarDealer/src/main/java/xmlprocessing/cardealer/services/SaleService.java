package xmlprocessing.cardealer.services;

import javax.xml.bind.JAXBException;

public interface SaleService {

    void seedSales();

    void writeAllSalesWithDiscount() throws JAXBException;
}
