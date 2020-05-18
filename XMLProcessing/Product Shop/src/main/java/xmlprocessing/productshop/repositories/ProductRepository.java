package xmlprocessing.productshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xmlprocessing.productshop.domain.entities.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product as p where p.price >:low and p.price <:high and p.buyer is null")
    List<Product> findProductsInRange(BigDecimal low, BigDecimal high);
}
