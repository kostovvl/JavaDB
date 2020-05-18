package softuni.exam.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.seedteamdtos.SeedTeamsDto;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtils;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;


import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;


@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final PictureRepository pictureRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper mapper;
    private final XmlParser parser;
    private final ValidatorUtil validator;
    private final FileUtils fileUtils;

    @Autowired
    public TeamServiceImpl(PictureRepository pictureRepository,
                           TeamRepository teamRepository, ModelMapper mapper,
                           XmlParser parser, ValidatorUtil validator, FileUtils fileUtils) {
        this.pictureRepository = pictureRepository;
        this.teamRepository = teamRepository;
        this.mapper = mapper;
        this.parser = parser;
        this.validator = validator;
        this.fileUtils = fileUtils;
    }

    @Transactional
    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        StringBuilder result = new StringBuilder();
        SeedTeamsDto seedTeamsDto = this.parser.importFromXml(SeedTeamsDto.class, Constants.TEAMS_URL);
        seedTeamsDto.getTeems().forEach(
                dto -> {
                    if (this.validator.isValid(dto)) {
                        if (this.teamRepository.findByName(dto.getName()) == null) {
                            Team team = this.mapper.map(dto, Team.class);
                            team.setPicture(this.pictureRepository.findByUrl(dto.getPicture().getUrl()));
                            this.teamRepository.saveAndFlush(team);
                            result.append("Successfully imported - ").append(team.getName()).append(System.lineSeparator());
                        } else {
                            result.append("Team already exists in DB").append(System.lineSeparator());
                        }
                    } else {
                        result.append("Invalid team").append(System.lineSeparator());
                    }
                }
        );
       return result.toString();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return this.fileUtils.readFileContent(Constants.TEAMS_URL);
    }

}
