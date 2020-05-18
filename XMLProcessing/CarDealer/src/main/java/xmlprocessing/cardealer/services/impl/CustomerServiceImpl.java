package xmlprocessing.cardealer.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xmlprocessing.cardealer.config.Constants;
import xmlprocessing.cardealer.domain.dtos.customerswithsalesdtos.CustomerWithSalesDto;
import xmlprocessing.cardealer.domain.dtos.customerswithsalesdtos.CustomersWithSalesDto;
import xmlprocessing.cardealer.domain.dtos.orderedcustomerdto.OrderedCustomerDto;
import xmlprocessing.cardealer.domain.dtos.orderedcustomerdto.OrderedCustomersDto;
import xmlprocessing.cardealer.domain.dtos.seedcustomerdto.SeedCustomersDto;
import xmlprocessing.cardealer.domain.entities.Customer;
import xmlprocessing.cardealer.domain.entities.Part;
import xmlprocessing.cardealer.domain.entities.Sale;
import xmlprocessing.cardealer.repositories.CustomerRepository;
import xmlprocessing.cardealer.repositories.SupplierRepository;
import xmlprocessing.cardealer.services.CustomerService;
import xmlprocessing.cardealer.utils.ValidatorUtil;
import xmlprocessing.cardealer.utils.XmlParserUtil;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ValidatorUtil validator;
    private final ModelMapper mapper;
    private final CustomerRepository repository;
    private final XmlParserUtil parser;

    @Autowired
    public CustomerServiceImpl(ValidatorUtil validator,
                               ModelMapper mapper, CustomerRepository repository, XmlParserUtil parser) {
        this.validator = validator;
        this.mapper = mapper;
        this.repository = repository;
        this.parser = parser;
    }

    @Override
    public void seedCustomers() throws JAXBException, FileNotFoundException {
        if (this.repository.count() != 0) {
            return;
        }
        SeedCustomersDto seedCustomersDto = this.parser.importFromXml(SeedCustomersDto.class, Constants.CUSTOMERS_URL);
        seedCustomersDto.getCustomerDtos().stream()
                .map(cdto -> {
                    Customer customer = this.mapper.map(cdto, Customer.class);
                    LocalDate birthDate = createBirthDate(cdto.getBirthDate());
                    customer.setDateOfBirth(birthDate);
                    return customer;
                }).forEach(this.repository::saveAndFlush);
    }

    private LocalDate createBirthDate(String birthDate) {
        String[] dateInput = birthDate.substring(0, 10).split("-");
        String dateParse = dateInput[2] + "/" + dateInput[1] + "/" + dateInput[0];
        return LocalDate.parse(dateParse, DateTimeFormatter.ofPattern("d/M/yyyy"));
    }

    @Override
    public void writeOrderedCustomers() throws JAXBException {
        OrderedCustomersDto orderedCustomersDto = new OrderedCustomersDto();
        orderedCustomersDto.setCustomers(
                this.repository.findAllCustomersOrOrderByTheDateOfBirth().stream()
                        .map(c -> {
                            OrderedCustomerDto orderedCustomerDto = this.mapper.map(c, OrderedCustomerDto.class);
                            String birthDate = c.getDateOfBirth() + "01T00:00:00";
                            orderedCustomerDto.setBirthDate(birthDate);
                            return orderedCustomerDto;
                        }).collect(Collectors.toList())
        );
        this.parser.exportToXML(orderedCustomersDto, Constants.ORDERED_CUSTOMERS_URL);
    }

    @Transactional
    @Override
    public void writeTotalSalesByCustomer() throws JAXBException {
        List<Customer> customers = this.repository.findAllCustomersWithMoreThanOnePurchase();
        CustomersWithSalesDto customersWithSalesDto = new CustomersWithSalesDto();
        customersWithSalesDto.setCustomers(
                customers.stream().map(c -> {
                    CustomerWithSalesDto customerWithSalesDto = this.mapper.map(c, CustomerWithSalesDto.class);
                           customerWithSalesDto.setBoughtCars(c.getSales().size());
                    BigDecimal totalMoneySpent = calculateTotalMoneySpent(c.getSales());
                    customerWithSalesDto.setTotalMoneySpent(totalMoneySpent);
                    return customerWithSalesDto;
                        }
                ).collect(Collectors.toList())
        );
        this.parser.exportToXML(customersWithSalesDto, Constants.SALES_BY_CUSTOMER_URL);
    }

    private BigDecimal calculateTotalMoneySpent(Set<Sale> sales) {
        BigDecimal result = new BigDecimal("0");
        for (Sale sale : sales) {
            for (Part part : sale.getCar().getParts()) {
                result = result.add(part.getPrice());
            }
        }
        return result;
    }
}
