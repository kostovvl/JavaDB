package productshop.repositories;

import productshop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("select u from User as u inner join u.selling as s" +
            " where s.buyer is not null group by u.id")
    List<User> findUserWithSoldItem();

    @Query("select u from User as u inner join u.selling as s" +
            " where s.buyer is not null group by u.id order by u.selling.size desc, u.lastName asc ")
    List<User> findUserWithSoldItemOrdered();
}
