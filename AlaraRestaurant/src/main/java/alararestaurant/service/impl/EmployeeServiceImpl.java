package alararestaurant.service.impl;

import alararestaurant.config.GlobalConstants;
import alararestaurant.domain.dtos.SeedEmployeeDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Position;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.PositionRepository;
import alararestaurant.service.EmployeeService;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtil validator;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PositionRepository positionRepository,
                               FileUtil reader, StringBuilder result, Gson gson, ModelMapper mapper, ValidationUtil validator) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.reader = reader;
        this.result = result;
        this.gson = gson;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Boolean employeesAreImported() {
       return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesJsonFile() throws IOException {
     return this.reader.readFile(GlobalConstants.EMPLOYEES_URL);
    }

    @Override
    public String importEmployees(String employees) throws IOException {
        SeedEmployeeDto[] seedEmployeeDtos = this.gson.fromJson(readEmployeesJsonFile(), SeedEmployeeDto[].class);

        for (SeedEmployeeDto employeeDto : seedEmployeeDtos) {
            if (this.validator.isValid(employeeDto)) {
                if (this.employeeRepository.findByName(employeeDto.getName()) == null) {
                    if(this.positionRepository.findByName(employeeDto.getPosition()) == null) {
                        seedPositionInDb(employeeDto.getPosition());
                    }
                    Position position = this.positionRepository.findByName(employeeDto.getPosition());
                    Employee employee = this.mapper.map(employeeDto, Employee.class);
                    employee.setPosition(position);
                    this.employeeRepository.saveAndFlush(employee);
                    this.result.append(String.format("Record %s successfully imported.%n", employeeDto.getName()));
                } else {
                    this.result.append("Already in DB.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Invalid data format.").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    private void seedPositionInDb(String position) {
        Position position1 = new Position();
        position1.setName(position);
        this.positionRepository.saveAndFlush(position1);
    }
}
