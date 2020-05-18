package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select p from Player as p where p.team.name =:teamName order by p.id")
    List<Player> findPlayersFromTeamNorthHubb(String teamName);

    @Query("select p from Player as p where p.salary>:salary order by p.salary desc ")
    List<Player> findPlayersWithSalaryHigherThan(BigDecimal salary);
}
