package project.services.interfaces;

import project.entities.Category;

import java.io.IOException;

public interface CategoryService {

    Category findCategoryByID(int id);

    void seedCategory() throws IOException;
}
