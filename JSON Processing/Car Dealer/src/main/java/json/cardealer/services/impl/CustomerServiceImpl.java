package json.cardealer.services.impl;

import com.google.gson.Gson;
import json.cardealer.config.Constants;
import json.cardealer.domain.dtos.createdtos.CreateCustomerDto;
import json.cardealer.domain.dtos.writedtos.WriteCustomerTotalSalesDto;
import json.cardealer.domain.dtos.writedtos.WriteCustomerWithBirthDateDto;
import json.cardealer.domain.entites.Customer;
import json.cardealer.domain.entites.Part;
import json.cardealer.domain.entites.Sale;
import json.cardealer.repositories.CustomerRepository;
import json.cardealer.services.CustomerService;
import json.cardealer.utils.FilesUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final FilesUtil filesUtil;
    private final Gson gson;
    private final ModelMapper mapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(FilesUtil filesUtil, Gson gson, ModelMapper mapper,
                               CustomerRepository customerRepository) {
        this.filesUtil = filesUtil;
        this.gson = gson;
        this.mapper = mapper;

        this.customerRepository = customerRepository;
    }

    @Override
    public void seedCustomers() throws IOException {
        String customersInput = this.filesUtil.readFileContent(Constants.CUSTOMERS_FILE_URL);
        CreateCustomerDto[] createCustomerDtos =
                this.gson.fromJson(customersInput, CreateCustomerDto[].class);
        Arrays.stream(createCustomerDtos)
                .map(o -> {
                    Customer customer = this.mapper.map(o, Customer.class);
                    String[] dateInput = o.getBirthDate().substring(0,10).split("-");
                    String dateParse = dateInput[2] + "/" + dateInput[1] + "/" + dateInput[0];
                 LocalDate dateOfBirth = LocalDate.parse(dateParse, DateTimeFormatter.ofPattern("d/M/yyyy"));
                  customer.setDateOfBirth(dateOfBirth);
                    return customer;
                }).forEach(this.customerRepository::saveAndFlush);
    }

    @Transactional
    @Override
    public void writeAllWithTotalSales() throws IOException {
        List<Customer> customers = this.customerRepository.findAllForTotalSales();
        List<WriteCustomerTotalSalesDto> writeCustomerTotalSalesDtos = new ArrayList<>();

        for (Customer customer : customers) {
            WriteCustomerTotalSalesDto writeCustomer = new WriteCustomerTotalSalesDto();
            writeCustomer.setFullName(customer.getName());
            writeCustomer.setBoughtCars(customer.getSales().size());
            BigDecimal totalMoneySpent = calculateTotalMoneySpent(customer);
            writeCustomer.setSpentMoney(totalMoneySpent);
            writeCustomerTotalSalesDtos.add(writeCustomer);
        }

        filesUtil.write(this.gson.toJson(writeCustomerTotalSalesDtos), Constants.TOTAL_SALES_BY_CUSTOMER_FILE_URL);
    }

    private BigDecimal calculateTotalMoneySpent(Customer customer) {
        BigDecimal result = new BigDecimal("0");
        for (Sale sale : customer.getSales()) {
            for (Part part : sale.getCar().getParts()) {
               result = result.add(part.getPrice());
            }
        }
        return result;
    }

    @Transactional
    @Override
    public void writeOrderedCustomers() throws IOException {
        List<WriteCustomerWithBirthDateDto> result =
                this.customerRepository.findAllDrivers()
                .stream()
                .map(o -> {
                    WriteCustomerWithBirthDateDto w = this.mapper.map(o, WriteCustomerWithBirthDateDto.class);
                 String birthDate = o.getDateOfBirth().toString() + "T00:00:00";
                    w.setDateOfBirth(birthDate);
                    return w;
                }).collect(Collectors.toList());

        this.filesUtil.write(this.gson.toJson(result), Constants.ORDERED_CUSTOMERS_FILE_URL);
    }
}
