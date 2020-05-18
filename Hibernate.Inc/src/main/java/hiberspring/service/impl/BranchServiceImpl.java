package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.SeedBranchDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.TownRepository;
import hiberspring.service.BranchService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final StringBuilder result;
    private final FileUtil reader;
    private final Gson gson;
    private final ValidationUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository,
                             StringBuilder result, FileUtil reader, Gson gson,
                             ValidationUtil validator, ModelMapper mapper) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.result = result;
        this.reader = reader;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.reader.readFile(Constants.BRANCHES_URL);
    }

    @Transactional
    @Override
    public String importBranches(String branchesFileContent) throws IOException {

        SeedBranchDto[] seedBranchDtos = this.gson.fromJson(readBranchesJsonFile(), SeedBranchDto[].class);

        for (SeedBranchDto seedBranchDto : seedBranchDtos) {
            if (this.validator.isValid(seedBranchDto)) {
                if (this.branchRepository.findByName(seedBranchDto.getName()) == null) {
                    Town town = this.townRepository.findByName(seedBranchDto.getTown());
                    if (town != null) {
                        Branch branch = this.mapper.map(seedBranchDto, Branch.class);
                        branch.setTown(town);
                        this.branchRepository.saveAndFlush(branch);
                        this.result.append(String.format("Successfully imported Branch %s.%n", branch.getName()));
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
}
