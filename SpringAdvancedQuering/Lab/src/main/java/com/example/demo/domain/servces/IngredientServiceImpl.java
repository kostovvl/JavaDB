package com.example.demo.domain.servces;

import com.example.demo.domain.entities.Ingredient;
import com.example.demo.domain.repositories.IngredientRepository;
import com.example.demo.domain.servces.interfaces.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> findByNameStartingWith(String model) {
        return this.ingredientRepository.findAllByNameStartingWith(model);
    }

    @Override
    public List<Ingredient> findByContainingIngredients(Set<String> ingredients) {
        return this.ingredientRepository.findContainingIngredients(ingredients);
    }

    @Override
    public void deleteIngredientByName(String name) {
        this.ingredientRepository.deleteIngredientsByName(name);
    }

    @Override
    public void updatePriceByPrice(BigDecimal price) {
        this.ingredientRepository.updatePriceByPrice(price);
    }

    @Override
    public void updatePriceByNameList(Set<String> names) {
        this.ingredientRepository.updatePriceByNameList(names);
    }
}