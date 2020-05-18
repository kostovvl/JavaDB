package com.example.demo.domain.servces.interfaces;

import com.example.demo.domain.entities.Shampoo;
import com.example.demo.domain.entities.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampooService {

    List<Shampoo> findBySize(Size size);

    List<Shampoo> findBySizeAndLabelId(Size size, Long labelId);

    List<Shampoo> findByPriceHigherThan(BigDecimal price);

    int countOfShampoosWithPriceLowerThan(BigDecimal price);

    List<Shampoo> selectFromListOfIngredients(Set<String> ingredients);

    List<Shampoo> selectFromIngredientsCount(int count);
}
