package xmlprocessing.cardealer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xmlprocessing.cardealer.domain.entities.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
}
