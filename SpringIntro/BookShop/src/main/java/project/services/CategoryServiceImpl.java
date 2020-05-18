package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.constants.FileLocations;
import project.entities.Category;
import project.repositories.CategoryRepository;
import project.services.interfaces.CategoryService;
import project.utils.FileUtil;

import java.io.IOException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileUtil fileUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public Category findCategoryByID(int id) {
        return this.categoryRepository.getOne(id);
    }

    @Override
    public void seedCategory() throws IOException {
        String[] fileData = this.fileUtil.readFile(FileLocations.CATEGORIES_PATH);
        for (String fileDatum : fileData) {
            if (this.categoryRepository.findByName(fileDatum) == null) {
                Category category = new Category(fileDatum);
                this.categoryRepository.saveAndFlush(category);
            }
        }
    }
}
