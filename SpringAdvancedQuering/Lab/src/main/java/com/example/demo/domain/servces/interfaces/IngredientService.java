package com.example.demo.domain.servces.interfaces;

import com.example.demo.domain.entities.Ingredient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface IngredientService {

    List<Ingredient> findByNameStartingWith(String model);

    List<Ingredient> findByContainingIngredients(Set<String> ingredients);

    void deleteIngredientByName(String name);

    void updatePriceByPrice(BigDecimal price);

    void updatePriceByNameList(Set<String> names);
}
