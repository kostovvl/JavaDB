package softuni.exam.service;


import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.seedplayerdtos.SeedPlayerDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.FileUtils;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;


import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PictureRepository pictureRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;
    private final ValidatorUtil validator;
    private final FileUtils fileUtils;
    private final Gson gson;

    public PlayerServiceImpl(PictureRepository pictureRepository, TeamRepository teamRepository,
                             PlayerRepository playerRepository, ModelMapper mapper,
                             ValidatorUtil validator, FileUtils fileUtils, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.fileUtils = fileUtils;
        this.gson = gson;
    }


    @Override
    public String importPlayers() throws IOException {
        StringBuilder result = new StringBuilder();
        SeedPlayerDto[] seedPlayerDtos = this.gson.fromJson(this.fileUtils.readFileContent(Constants.PLAYERS_URL),
                SeedPlayerDto[].class);
        System.out.println();
        for (SeedPlayerDto dto : seedPlayerDtos) {
            if (this.validator.isValid(dto)) {
             if (this.playerRepository.findByFirstNameAndLastName(dto.getFirstName(), dto.getLastName()) == null) {
                 Player player = this.mapper.map(dto, Player.class);
                 Picture picture = this.pictureRepository.findByUrl(dto.getPicture().getUrl());
                 Team team = this.teamRepository.findByName(dto.getTeam().getName());
                 if (team != null && picture != null) {
                     player.setTeam(team);
                     player.setPicture(picture);
                     this.playerRepository.saveAndFlush(player);
                     result.append("Successfully imported player: ")
                             .append(player.getFirstName() + " " + player.getLastName()).append(System.lineSeparator());
                 } else {
                     result.append("Invalid player").append(System.lineSeparator());
                 }
             } else {
                 result.append("Player already exists in DB").append(System.lineSeparator());
             }
            } else {
                result.append("Invalid player").append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return this.fileUtils.readFileContent(Constants.PLAYERS_URL);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder result = new StringBuilder();
        List<Player> players = this.playerRepository.findPlayersWithSalaryHigherThan(new BigDecimal("100000"));
        players.forEach(p -> {
            result.append(String.format("Player name: %s %s %n" +
                            "Number: %d%n" +
                            "Salary: %s%n" +
                            "Team: %s%n",
                    p.getFirstName(), p.getLastName(),
                    p.getNumber(), p.getSalary(), p.getTeam().getName()));

        });
        return result.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder result = new StringBuilder();
        List<Player> players = this.playerRepository.findPlayersFromTeamNorthHubb("North Hub");
        result.append("Team: North Hub").append(System.lineSeparator());
        players.forEach(p -> {
            result.append(String.format("Player name: %s %s - %s%n" +
                            "Number: %d%n", p.getFirstName(), p.getLastName(),
                    p.getPosition(), p.getNumber()));

        });
        return result.toString();
    }


}
