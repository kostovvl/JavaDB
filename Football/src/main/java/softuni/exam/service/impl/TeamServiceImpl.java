package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.SeedTeamDto;
import softuni.exam.domain.dtos.SeedTeamsDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.TeamService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final XmlParser parser;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository,
                           FileUtil reader, StringBuilder result, XmlParser parser,
                           ValidatorUtil validator, ModelMapper mapper) {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.reader = reader;
        this.result = result;
        this.parser = parser;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        SeedTeamsDto seedTeamsDto = this.parser.importFromXml(SeedTeamsDto.class, Constants.TEAMS_URL);

        for (SeedTeamDto teamDto : seedTeamsDto.getTeams()) {
            if (this.validator.isValid(teamDto)) {

                if (this.teamRepository.findByName(teamDto.getName()) == null) {
                    Picture picture = this.pictureRepository.findByUrl(teamDto.getPicture().getUrl());

                    if (picture != null) {

                        Team team = this.mapper.map(teamDto, Team.class);
                        team.setPicture(picture);
                        this.teamRepository.saveAndFlush(team);
                        this.result.append(String.format("Successfully imported - %s%n", team.getName()));

                    } else {
                        this.result.append("Invalid team").append(System.lineSeparator());
                    }

                } else {
                    this.result.append("Invalid team").append(System.lineSeparator());
                }

            } else {
                this.result.append("Invalid team").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    @Override
    public boolean areImported() {
      return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
       return this.reader.readFile(Constants.TEAMS_URL);
    }
}
