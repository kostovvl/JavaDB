package json.cardealer.repositories;

import json.cardealer.domain.entites.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

    Part findById(long id);
}
