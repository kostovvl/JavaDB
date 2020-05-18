package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByNameStartingWith(String model);

    @Query("select i from  Ingredient as i where i.name in :ingredients")
    List<Ingredient> findContainingIngredients(Set<String> ingredients);

    @Transactional
    @Modifying
    @Query("delete from Ingredient as i where i.name =:name")
    void deleteIngredientsByName(String name);

    @Transactional
    @Modifying
    @Query("update Ingredient as e set e.price = e.price * 1.1 where e.price =:price")
    void updatePriceByPrice(BigDecimal price);

    @Transactional
    @Modifying
    @Query("update Ingredient as e set e.price = e.price * 1.1 where e.name in :names")
    void updatePriceByNameList(Set<String> names);
}
