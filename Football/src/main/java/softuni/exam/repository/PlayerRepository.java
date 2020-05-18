package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByFirstNameAndLastName(String fn, String ln);

    @Query("select p from Player as p where p.team.name = 'North Hub' order by p.id")
    List<Player> firstExport();

    @Query("select p from Player as p where p.salary > 100000 order by p.salary desc")
    List<Player> secondExport();

}
