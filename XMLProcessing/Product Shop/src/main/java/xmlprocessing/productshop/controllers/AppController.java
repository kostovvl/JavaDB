package xmlprocessing.productshop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xmlprocessing.productshop.services.CategoryService;
import xmlprocessing.productshop.services.ProductService;
import xmlprocessing.productshop.services.UserService;

@Component
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AppController(CategoryService categoryService, UserService userService, ProductService productService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        //this.categoryService.seedCategories();
        //this.userService.seedUsers();
        //this.productService.seedProducts();

        //this.productService.writeProductsInRange();
       // this.userService.writeUsersWithSuccessfullySoldProducts();
        //this.categoryService.writeCategoriesByProductCount();
        this.userService.writeUsersWithSoldProducts();
    }
}
