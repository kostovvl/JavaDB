package gamestore.demo.services;

import gamestore.demo.domain.dtos.FindByTitleDto;
import gamestore.demo.domain.dtos.PrintGameDto;
import gamestore.demo.domain.dtos.RegisterGameDto;
import gamestore.demo.domain.entities.Game;

import java.util.List;

public interface GameService {

    void registerGame(RegisterGameDto registerGameDto);

    void deleteById(long id);

    void editGame(String[] input);

    List<PrintGameDto> findAll();

    FindByTitleDto findByTitle(String title);

}
