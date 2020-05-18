package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.Constants;
import softuni.exam.domain.dtos.SeedPlayerDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PlayerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PictureRepository pictureRepository;
    private final TeamRepository teamRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final Gson gson;
    private final ValidatorUtil validator;
    private final ModelMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PictureRepository pictureRepository, TeamRepository teamRepository,
                             FileUtil reader, StringBuilder result, Gson gson, ValidatorUtil validator, ModelMapper mapper) {
        this.playerRepository = playerRepository;
        this.pictureRepository = pictureRepository;
        this.teamRepository = teamRepository;
        this.reader = reader;
        this.result = result;
        this.gson = gson;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public String importPlayers() throws IOException {
        SeedPlayerDto[] seedPlayerDtos = this.gson.fromJson(this.readPlayersJsonFile(), SeedPlayerDto[].class);

        for (SeedPlayerDto seedPlayerDto : seedPlayerDtos) {
            if (this.validator.isValid(seedPlayerDto)) {
                String fn = seedPlayerDto.getFirstName();
                String ln = seedPlayerDto.getLastName();
                if (this.playerRepository.findByFirstNameAndLastName(fn, ln) == null) {

                    Team team = getTeamFromTeamRepository(seedPlayerDto);
                    Picture picture = getPictureFromPictureRepositry(seedPlayerDto);

                    if (team != null && picture != null) {

                        Player player = this.mapper.map(seedPlayerDto, Player.class);
                        player.setTeam(team);
                        player.setPicture(picture);
                        this.playerRepository.saveAndFlush(player);
                        this.result.append(String.format("Successfully imported player: %s %s%n",
                                player.getFirstName(), player.getLastName()));

                    } else {
                        this.result.append("Invalid player").append(System.lineSeparator());
                    }

                } else {
                    this.result.append("Invalid player").append(System.lineSeparator());
                }

            } else {
                this.result.append("Invalid player").append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    private Picture getPictureFromPictureRepositry(SeedPlayerDto seedPlayerDto) {

        if (this.pictureRepository.findByUrl(seedPlayerDto.getPicture().getUrl()) != null) {
            return pictureRepository.findByUrl(seedPlayerDto.getPicture().getUrl());
        }

        return null;
    }

    private Team getTeamFromTeamRepository(SeedPlayerDto seedPlayerDto) {
        if (this.teamRepository.findByName(seedPlayerDto.getTeam().getName()) != null) {
             return teamRepository.findByName(seedPlayerDto.getTeam().getName());
        }

        return null;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return this.reader.readFile(Constants.PLAYERS_URL);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        this.result.delete(0, this.result.length());

        List<Player> players = this.playerRepository.secondExport();

        for (Player player : players) {
            result.append(String.format("Player name: %s %s%n" +
                            "Number: %d%n" +
                            "Salary: %s%n" +
                            "Team: %s%n" +
                            ". . .%n",
                    player.getFirstName(), player.getLastName(),
                    player.getNumber(), player.getSalary(), player.getTeam().getName()));
        }

        return this.result.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        this.result.delete(0, this.result.length());

        List<Player> players = this.playerRepository.firstExport();

        result.append("Team: North Hub").append(System.lineSeparator());
        for (Player player : players) {
            result.append(String.format("Player name: %s %s - %s%n" +
                    "Number: %d%n",
                    player.getFirstName(), player.getLastName(), player.getPosition(), player.getNumber()));
        }
        return this.result.toString();
    }
}
