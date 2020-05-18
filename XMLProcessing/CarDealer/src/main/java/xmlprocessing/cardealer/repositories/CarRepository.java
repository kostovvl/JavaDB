package xmlprocessing.cardealer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xmlprocessing.cardealer.domain.entities.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from Car as c where c.make = 'Toyota' order by c.model")
    List<Car> findAllCarsMadeByToyota();

    List<Car> findAll();
}
