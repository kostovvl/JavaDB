package json.cardealer.repositories;

import json.cardealer.domain.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findById(long id);

    @Query("select c from Customer as c where c.sales.size > 1")
    List<Customer> findAllForTotalSales();

    @Query("select d from Customer as d order by d.dateOfBirth asc , d.youngDriver")
    List<Customer> findAllDrivers();
}
