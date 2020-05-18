package json.cardealer.repositories;

import json.cardealer.domain.entites.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Transactional
    Car findById(long id);

    List<Car> findAllByMake(String make);

    List<Car> findAll();
}
