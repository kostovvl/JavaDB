package xmlprocessing.cardealer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xmlprocessing.cardealer.services.*;

@Component
public class AppController implements CommandLineRunner {

    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;

    @Autowired
    public AppController(SupplierService supplierService, PartService partService,
                         CarService carService, CustomerService customerService, SaleService saleService) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
    }


    @Override
    public void run(String... args) throws Exception {
//        this.supplierService.seedSuppliers();
//        this.partService.seedParts();
//        this.carService.seedCars();
//        this.customerService.seedCustomers();
//        this.saleService.seedSales();

        //this.customerService.writeOrderedCustomers();
       // this.carService.writeCarsFromMakeToyota();
        //this.supplierService.writeLocalSuppliers();
       // this.carService.writeCarsWithTheirParts();
       // this.customerService.writeTotalSalesByCustomer();
       // this.saleService.writeAllSalesWithDiscount();
    }
}
