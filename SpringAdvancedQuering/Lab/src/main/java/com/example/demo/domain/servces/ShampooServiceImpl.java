package com.example.demo.domain.servces;

import com.example.demo.domain.entities.Shampoo;
import com.example.demo.domain.entities.Size;
import com.example.demo.domain.repositories.ShampooRepository;
import com.example.demo.domain.servces.interfaces.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class ShampooServiceImpl implements ShampooService {

    private final ShampooRepository shampooRepository;

    @Autowired
    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<Shampoo> findBySize(Size size) {

        return this.shampooRepository.findAllBySizeOrderById(size);
    }

    @Override
    public List<Shampoo> findBySizeAndLabelId(Size size, Long labelId) {
        return this.shampooRepository.findBySizeOrLabelIdOrderByPriceAsc(size, labelId);
    }

    @Override
    public List<Shampoo> findByPriceHigherThan(BigDecimal price) {
        return this.shampooRepository.findByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public int countOfShampoosWithPriceLowerThan(BigDecimal price) {
        return this.shampooRepository.countAllByPriceIsLessThan(price);
    }

    @Override
    public List<Shampoo> selectFromListOfIngredients(Set<String> ingredients) {
        return this.shampooRepository.selectFromList(ingredients);
    }

    @Override
    public List<Shampoo> selectFromIngredientsCount(int count) {
        return this.shampooRepository.selectByIngredientCount(count);
    }
}
