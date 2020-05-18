package hiberspring.service.impl;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.seedemployeesdtos.SeedEmployeeDto;
import hiberspring.domain.dtos.seedemployeesdtos.SeedEmployeesDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final BranchRepository branchRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final XmlParser parser;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeCardRepository employeeCardRepository,
                               BranchRepository branchRepository, FileUtil reader,
                               StringBuilder result, XmlParser parser, ValidationUtil validator, ModelMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.branchRepository = branchRepository;
        this.reader = reader;
        this.result = result;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.reader.readFile(Constants.EMPLOYEES_URL);
    }

    @Transactional
    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {

        SeedEmployeesDto seedEmployeesDto = this.parser.readXml(SeedEmployeesDto.class, Constants.EMPLOYEES_URL);

        for (SeedEmployeeDto employeeDto : seedEmployeesDto.getEmployees()) {
            if (this.validator.isValid(employeeDto)) {
                String fn = employeeDto.getFirstName();
                String ln = employeeDto.getLastName();
                if (this.employeeRepository.findByFirstNameAndLastName(fn, ln) == null) {
                    EmployeeCard employeeCard = this.employeeCardRepository.findByNumber(employeeDto.getCard());
                    Branch branch = this.branchRepository.findByName(employeeDto.getBranch());

                    if (employeeCard != null && branch != null) {
                        Employee employee = this.mapper.map(employeeDto, Employee.class);
                        employee.setCard(employeeCard);
                        employee.setBranch(branch);
                        this.employeeRepository.saveAndFlush(employee);
                        this.result.append(String.format("Successfully imported Employee %s %s.%n",
                                employee.getFirstName(), employee.getLastName()));
                    } else {
                        this.result.append("Error: Invalid data.").append(System.lineSeparator());
                    }
                } else {
                    this.result.append("Already in DB.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Error: Invalid data.").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    @Override
    public String exportProductiveEmployees() {
        this.result.delete(0, result.length());

        List<Employee> employees = this.employeeRepository.export();

        for (Employee employee : employees) {
            this.result.append(String.format("Name: %s %s%n" +
                    "Position: %s%n" +
                    "Card Number: %s%n" +
                    "-------------------------%n",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getPosition(), employee.getCard().getNumber()));
        }
        return this.result.toString();
    }
}
