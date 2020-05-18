package gamestore.demo.repositories;

import gamestore.demo.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {


    void deleteById(long id);

    Game findById(long id);

    List<Game> findAll();

    Game findByTitle(String title);
}
