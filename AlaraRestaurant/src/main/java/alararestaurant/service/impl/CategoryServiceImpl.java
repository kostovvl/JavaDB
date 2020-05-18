package alararestaurant.service.impl;

import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.service.CategoryService;
import alararestaurant.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final StringBuilder result;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, StringBuilder result) {
        this.categoryRepository = categoryRepository;
        this.result = result;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        List<Category> categories = this.categoryRepository.firstExport();

        for (Category category : categories) {
            this.result.append(String.format("Category: %s%n", category.getName()));
            for (Item item : category.getItems()) {
                this.result.append(String.format("--- Item Name: %s%n" +
                        "--- Item Price: %s%n", item.getName(), item.getPrice()));
            }

        }

        return this.result.toString();
    }
}
