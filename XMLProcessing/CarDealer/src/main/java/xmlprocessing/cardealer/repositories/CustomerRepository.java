package xmlprocessing.cardealer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xmlprocessing.cardealer.domain.entities.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer as c order by c.dateOfBirth asc ")

    List<Customer> findAllCustomersOrOrderByTheDateOfBirth();

    @Query("select c from Customer as c where c.sales.size > 0")
    List<Customer> findAllCustomersWithMoreThanOnePurchase();
}
