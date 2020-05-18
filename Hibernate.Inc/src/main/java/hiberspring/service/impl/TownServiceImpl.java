package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.SeedTownDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, FileUtil reader,
                           StringBuilder result, Gson gson, ValidationUtil validator, ModelMapper mapper) {
        this.townRepository = townRepository;
        this.reader = reader;
        this.result = result;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.reader.readFile(Constants.TOWNS_URL);
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {

        SeedTownDto[] seedTownDtos = this.gson.fromJson(readTownsJsonFile(), SeedTownDto[].class);

        for (SeedTownDto seedTownDto : seedTownDtos) {
            if (this.validator.isValid(seedTownDto)) {
                if (this.townRepository.findByName(seedTownDto.getName()) == null) {
                    Town town = this.mapper.map(seedTownDto, Town.class);
                    this.townRepository.saveAndFlush(town);
                    this.result.append(String.format("Successfully imported Town %s.%n", town.getName()));
                } else {
                    this.result.append("Already in Db.").append(System.lineSeparator());
                }
            } else {
                this.result.append("Error: Invalid data.").append(System.lineSeparator());
            }
        }

        return this.result.toString();
    }
}
