package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Picture;

import java.util.List;
import java.util.Set;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    Picture findByName(String name);

    @Query("select p from Picture as p where p.car.id =:id")
    Set<Picture> findAllByIdOfCar(long id);

}
