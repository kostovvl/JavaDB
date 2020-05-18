package gamestore.demo.services.impl;

import gamestore.demo.domain.dtos.EditGameDto;
import gamestore.demo.domain.dtos.FindByTitleDto;
import gamestore.demo.domain.dtos.PrintGameDto;
import gamestore.demo.domain.dtos.RegisterGameDto;
import gamestore.demo.domain.entities.Game;
import gamestore.demo.domain.entities.Status;
import gamestore.demo.repositories.GameRepository;
import gamestore.demo.services.GameService;
import gamestore.demo.services.UserService;
import gamestore.demo.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final ModelMapper modelMapper;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final ValidatorUtil validatorUtil;
    @Autowired
    public GameServiceImpl(ModelMapper modelMapper, GameRepository gameRepository,
                           UserService userService, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public void registerGame(RegisterGameDto registerGameDto) {
        if (this.userService.getLoggedUser() == null) {
            System.out.println("No user logged!");
            return;
        } else if (userService.getLoggedUser().getStatus().equals(Status.USER)) {
            System.out.println("Admin must be logged in order to add a game!");
            return;
        }

        Game game = this.modelMapper.map(registerGameDto, Game.class);
        System.out.println();
        this.gameRepository.saveAndFlush(game);
        System.out.println("Added " + game.getTitle());
    }

    @Override
    public void deleteById(long id) {
        if (this.userService.getLoggedUser() == null) {
            System.out.println("No user logged!");
            return;
        } else if (userService.getLoggedUser().getStatus().equals(Status.USER)) {
            System.out.println("Admin must be logged in order to add a game!");
            return;
        }

        try{
            this.gameRepository.deleteById(id);
            System.out.println("Successfully deleted game with id " + id);
        } catch (Exception e) {
            System.out.printf("Game with id %s does not exist in the database!", id);
        }
    }

    @Transactional
    @Override
    public void editGame(String[] input) {

        if (this.userService.getLoggedUser() == null) {
            System.out.println("No user logged!");
            return;
        } else if (userService.getLoggedUser().getStatus().equals(Status.USER)) {
            System.out.println("Admin must be logged in order to add a game!");
            return;
        }

        Game game = this.gameRepository.findById(Long.parseLong(input[1]));
        if (game == null) {
            System.out.printf("No game with Id %s exists in the database%n", input[1]);
            return;
        }
        this.gameRepository.deleteById(game.getId());

        EditGameDto editGameDto = this.modelMapper.map(game, EditGameDto.class);
        editEditGameDto(input, editGameDto);

        if (this.validatorUtil.isValid(editGameDto)) {
            game = this.modelMapper.map(editGameDto, Game.class);
            this.gameRepository.saveAndFlush(game);
            System.out.printf("Game %s edited!%n", game.getTitle());
        } else {
            this.validatorUtil.getViolations(editGameDto)
                    .stream().map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Override
    public List<PrintGameDto> findAll() {
        return this.gameRepository.findAll()
                .stream().map(g -> {
                    PrintGameDto printGameDto = new PrintGameDto();
                    printGameDto.setTitle(g.getTitle());
                    printGameDto.setPrice(g.getPrice());
                    return printGameDto;
                }).collect(Collectors.toList());
    }

    @Override
    public FindByTitleDto findByTitle(String title) {
        return modelMapper.map(this.gameRepository.findByTitle(title), FindByTitleDto.class);
    }

    private void editEditGameDto(String[] input, EditGameDto editGameDto) {

        for (int i = 2; i < input.length; i++) {
            String[] s = input[i].split("=");
            String field = s[0];
            String value = s[1];
            if (field.equals("title")){
                editGameDto.setTitle(value);
            }
            else if (field.equals("trailer")) {
                editGameDto.setTrailer(value);
            }
            else if (field.equals("image")) {
                editGameDto.setImage(value);
            }
            else if (field.equals("size")) {
                editGameDto.setSize(Double.parseDouble(value));
            } else if (field.equals("price")) {
                editGameDto.setPrice(new BigDecimal(value));
            }
            else if (field.equals("description")) {
                editGameDto.setDescription(value);
            }
            else if (field.equals("releaseDate")) {
                editGameDto.setReleaseDate(LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
        }

    }
}
