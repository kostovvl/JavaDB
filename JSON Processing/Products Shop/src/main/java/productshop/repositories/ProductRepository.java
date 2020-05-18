package productshop.repositories;

import productshop.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product as p where p.buyer is null and p.price >:low and p.price <:high")
    List<Product> findProductInPriceRange (BigDecimal low, BigDecimal high);
}
