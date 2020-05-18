package json.cardealer.services.impl;

import com.google.gson.Gson;
import json.cardealer.config.Constants;
import json.cardealer.domain.dtos.writedtos.WriteSaleDto;
import json.cardealer.domain.entites.Car;
import json.cardealer.domain.entites.Customer;
import json.cardealer.domain.entites.Sale;
import json.cardealer.repositories.CarRepository;
import json.cardealer.repositories.CustomerRepository;
import json.cardealer.repositories.SaleRepository;
import json.cardealer.services.SaleService;
import json.cardealer.utils.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final Random random;
    private final HashMap<Integer, Double> discountRates;
    private final ModelMapper mapper;
    private final FilesUtil filesUtil;
    private final Gson gson;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, CarRepository carRepository,
                           CustomerRepository customerRepository, Random random, ModelMapper mapper, FilesUtil filesUtil, Gson gson) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.random = random;
        this.mapper = mapper;
        this.filesUtil = filesUtil;
        this.gson = gson;
        this.discountRates = new HashMap<>();
        discountRates.put(0, 0.);
        discountRates.put(1, 5.);
        discountRates.put(2, 10.);
        discountRates.put(3, 15.);
        discountRates.put(4, 20.);
        discountRates.put(5, 30.);
        discountRates.put(6, 40.);
        discountRates.put(7, 50.);
    }
    
    @Transactional
    @Override
    public void seedSales() {
        if (this.saleRepository.count() != 0) {
            return;
        }

        for (int i = 0; i < 30; i++) {
            Sale sale = new Sale();
            sale.setCar(getRandomCar());
            sale.setCustomer(getRandomCustomer());
            sale.setDiscount(generateDiscount(sale.getCustomer()));
            this.saleRepository.saveAndFlush(sale);
        }

    }


    private double generateDiscount(Customer customer) {
        double additionalDiscount = customer.isYoungDriver()? 5. : 0.;
        double mainDiscount = this.discountRates.get(this.random.nextInt(8));
        return additionalDiscount + mainDiscount;
    }

    private Customer getRandomCustomer() {
        long customerId = this.random.nextInt((int)this.customerRepository.count()) + 1;
        return this.customerRepository.findById(customerId);
    }

    private Car getRandomCar() {
        long carId = this.random.nextInt((int)this.carRepository.count()) + 1;
        return this.carRepository.findById(carId);
    }

    @Transactional
    @Override
    public void writeSalesWithAppliedDiscount() throws IOException {

        List<WriteSaleDto> writeSaleDtos =
                this.saleRepository.findAll()
                .stream()
                .map(o -> {
                    WriteSaleDto writeSaleDto = this.mapper.map(o, WriteSaleDto.class);
                    writeSaleDto.calculatePrice();
                    writeSaleDto.calculatePriceWithDiscount();
                    writeSaleDto.setCustomer(o.getCustomer().getName());
                    return writeSaleDto;
                }).collect(Collectors.toList());

        this.filesUtil.write(this.gson.toJson(writeSaleDtos), Constants.SALES_WITH_DISCOUNT_FILE_URL);

    }

}
