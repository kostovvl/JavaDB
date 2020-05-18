package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.SeedEmployeeCardDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {

    private final EmployeeCardRepository employeeCardRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, FileUtil reader,
                                   StringBuilder result, Gson gson, ValidationUtil validator, ModelMapper mapper) {
        this.employeeCardRepository = employeeCardRepository;
        this.reader = reader;
        this.result = result;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.reader.readFile(Constants.EMPLOYEE_CARDS_URL);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {

        SeedEmployeeCardDto[] seedEmployeeCardDtos = this.gson.fromJson(readEmployeeCardsJsonFile(), SeedEmployeeCardDto[].class);

        for (SeedEmployeeCardDto seedEmployeeCardDto : seedEmployeeCardDtos) {
            if (this.validator.isValid(seedEmployeeCardDto)) {
                if (this.employeeCardRepository.findByNumber(seedEmployeeCardDto.getNumber()) == null) {
                    EmployeeCard employeeCard = this.mapper.map(seedEmployeeCardDto, EmployeeCard.class);
                    this.employeeCardRepository.saveAndFlush(employeeCard);
                    this.result.append(String.format("Successfully imported Employee Card %s.%n", employeeCard.getNumber()));
                } else {
                    this.result.append("Already in DB.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Error: Invalid data.").append(System.lineSeparator());
            }
        }

        return this.result.toString();
    }
}
