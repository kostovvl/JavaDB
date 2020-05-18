package com.example.demo.domain.repositories;

import com.example.demo.domain.entities.Shampoo;
import com.example.demo.domain.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampooRepository extends JpaRepository<Shampoo, Long> {


    List<Shampoo> findAllBySizeOrderById(Size size);

    List<Shampoo> findBySizeOrLabelIdOrderByPriceAsc(Size size, Long labelId);

    List<Shampoo> findByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    int countAllByPriceIsLessThan(BigDecimal price);

    @Query("select s from Shampoo as s inner join s.ingredients as i where i.name in :ingredients")
    List<Shampoo> selectFromList(Set<String> ingredients);

    @Query("select s from Shampoo as s where s.ingredients.size <:count")
    List<Shampoo> selectByIngredientCount(int count);
}
