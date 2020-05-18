package json.cardealer.controllers;

import json.cardealer.domain.entites.Car;
import json.cardealer.repositories.CarRepository;
import json.cardealer.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppController implements CommandLineRunner {

    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;
    private final CarRepository carRepository;


    @Autowired
    public AppController(SupplierService supplierService, PartService partService,
                         CarService carService, CustomerService customerService, SaleService saleService, CarRepository carRepository) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;

        this.customerService = customerService;
        this.saleService = saleService;
        this.carRepository = carRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedCars();
        this.customerService.seedCustomers();
       this.saleService.seedSales();

      // this.customerService.writeOrderedCustomers();
       // this.carService.writeAllToyotas();
       // this.supplierService.writeLocalSuppliers();
        //this.carService.writeCarsWithTheirListOfParts();
        //this.customerService.writeAllWithTotalSales();
       // this.saleService.writeSalesWithAppliedDiscount();
    }
}
