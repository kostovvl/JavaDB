package xmlprocessing.cardealer.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import xmlprocessing.cardealer.config.Constants;
import xmlprocessing.cardealer.domain.dtos.carswithpartsdto.CarDto;
import xmlprocessing.cardealer.domain.dtos.saleswithapplieddiscountdtos.CarSaleDto;
import xmlprocessing.cardealer.domain.dtos.saleswithapplieddiscountdtos.SaleWithDiscountDto;
import xmlprocessing.cardealer.domain.dtos.saleswithapplieddiscountdtos.SalesWithDiscountDto;
import xmlprocessing.cardealer.domain.entities.Car;
import xmlprocessing.cardealer.domain.entities.Customer;
import xmlprocessing.cardealer.domain.entities.Part;
import xmlprocessing.cardealer.domain.entities.Sale;
import xmlprocessing.cardealer.repositories.CarRepository;
import xmlprocessing.cardealer.repositories.CustomerRepository;
import xmlprocessing.cardealer.repositories.SaleRepository;
import xmlprocessing.cardealer.services.SaleService;
import xmlprocessing.cardealer.utils.XmlParserUtil;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
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
    private final XmlParserUtil parser;
    private final ModelMapper mapper;

    public SaleServiceImpl(SaleRepository saleRepository, CarRepository carRepository,
                           CustomerRepository customerRepository, Random random, XmlParserUtil parser, ModelMapper mapper) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.random = random;
        this.parser = parser;
        this.mapper = mapper;
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

    @Transactional
    @Override
    public void writeAllSalesWithDiscount() throws JAXBException {
        SalesWithDiscountDto salesWithDiscountDto = new SalesWithDiscountDto();
       List<SaleWithDiscountDto> saleWithDiscountDtoList = this.saleRepository.findAll().stream()
                .map(s -> {
                    SaleWithDiscountDto saleWithDiscountDto = new SaleWithDiscountDto();
                    CarSaleDto carDto = this.mapper.map(s.getCar(), CarSaleDto.class);
                    saleWithDiscountDto.setCarDto(carDto);
                    saleWithDiscountDto.setCustomer(s.getCustomer().getName());
                    saleWithDiscountDto.setDiscount(s.getDiscount());
                    BigDecimal totalPrice = calculatePrice(s.getCar());
                    BigDecimal priceWithDiscount = calculatePriceWithDiscount(totalPrice, s.getDiscount());
                    saleWithDiscountDto.setPrice(totalPrice);
                    saleWithDiscountDto.setPriceWithDiscount(priceWithDiscount);
                    System.out.println();
                    return saleWithDiscountDto;
                }).collect(Collectors.toList());

        salesWithDiscountDto.setSales(saleWithDiscountDtoList);

        this.parser.exportToXML(salesWithDiscountDto, Constants.SALES_WITH_DISCOUNT_URL);
    }

    private BigDecimal calculatePriceWithDiscount(BigDecimal totalPrice, double discount) {
        MathContext context = new MathContext(6);
        BigDecimal result = totalPrice.subtract(totalPrice.multiply(new BigDecimal(discount/100)));
        return result.round(context);
    }

    private BigDecimal calculatePrice(Car car) {
        BigDecimal result = new BigDecimal("0");
        for (Part part : car.getParts()) {
            result = result.add(part.getPrice());
        }
        return result;
    }


    private double generateDiscount(Customer customer) {
        double additionalDiscount = customer.isYoungDriver() ? 5. : 0.;
        double mainDiscount = this.discountRates.get(this.random.nextInt(8));
        return additionalDiscount + mainDiscount;
    }

    private Customer getRandomCustomer() {
        long customerId = this.random.nextInt((int) this.customerRepository.count()) + 1;
        return this.customerRepository.findById(customerId).orElse(null);
    }

    private Car getRandomCar() {
        long carId = this.random.nextInt((int) this.carRepository.count()) + 1;
        return this.carRepository.findById(carId).orElse(null);
    }
}

