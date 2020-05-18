package gamestore.demo.controllers;

import gamestore.demo.domain.dtos.*;
import gamestore.demo.services.impl.GameServiceImpl;
import gamestore.demo.services.impl.UserServiceImpl;
import gamestore.demo.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
public class AppController implements CommandLineRunner {

    private final ValidatorUtil validator;
    private final GameServiceImpl gameService;
    private final UserServiceImpl userService;
    private final Scanner scanner;

    @Autowired
    public AppController(ValidatorUtil validator, GameServiceImpl gameService,
                         UserServiceImpl userService, Scanner scanner) {
        this.validator = validator;
        this.gameService = gameService;
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Enter command:");
            String[] input = scanner.nextLine().split("\\|");

            switch (input[0]) {
                case "RegisterUser": {
                    registerUser(input);
                    break;
                }
                case "LoginUser": {
                    loginUser(input);
                    break;
                }
                case "Logout": {
                    this.userService.logout();
                    break;
                }
                case "AddGame": {
                    addGame(input);
                    break;
                }
                case "EditGame": {
                    this.gameService.editGame(input);
                    break;
                }
                case "DeleteGame": {
                    this.gameService.deleteById(Long.parseLong(input[1]));
                    break;
                }
                case "AllGames": {
                    findAndPrintAllGames();
                    break;
                }
                case "DetailGame": {
                    findAndPrintGameByTitle(input);
                    break;
                }
                case "Exit": {
                    System.exit(0);
                    break;
                }
                default:{
                    System.out.println("Invalid Command");
                    break;
                }
            }
        }

    }

    private void findAndPrintGameByTitle(String[] input) {
        try {
        FindByTitleDto findByTitleDto = this.gameService.findByTitle(input[1]);
            System.out.printf("Title: %s%n" +
                            "Price: %.2f%n" +
                            "Description: %s%n" +
                            "Release date: %s%n", findByTitleDto.getTitle(), findByTitleDto.getPrice(),
                    findByTitleDto.getDescription(), findByTitleDto.getReleaseDate());
        } catch (IllegalArgumentException e) {
            System.out.println("No such game exists in the database!");
        }

    }

    private void findAndPrintAllGames() {
        List<PrintGameDto> printGameDtos = this.gameService.findAll();
        if (printGameDtos.isEmpty()) {
            System.out.println("Database is empty!");
            return;
        }
        printGameDtos.forEach(pgdto -> System.out.printf("%s %.2f%n",
                pgdto.getTitle(), pgdto.getPrice()));
    }

    private void addGame(String[] input) {
        RegisterGameDto registerGameDto =
                new RegisterGameDto(input[1], new BigDecimal(input[2]),
                        Double.parseDouble(input[3]), input[4], input[5],
                        input[6], LocalDate.parse(input[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println();
        if (this.validator.isValid(registerGameDto)) {
            this.gameService.registerGame(registerGameDto);
        } else {
            this.validator.getViolations(registerGameDto)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
    }

    private void loginUser(String[] input) {
        LoginUserDto loginUserDto = new LoginUserDto(input[1], input[2]);
        this.userService.loginUser(loginUserDto);
    }

    private void registerUser(String[] input) {
        if (!input[2].equals(input[3])) {
            System.out.println("Passwords don't match");
            return;
        }
        RegisterUserDto registerUserDto = new RegisterUserDto(input[1], input[2], input[4]);
        if (this.validator.isValid(registerUserDto)) {
            this.userService.registerUser(registerUserDto);
        } else {
            this.validator.getViolations(registerUserDto)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
        }
    }
}
