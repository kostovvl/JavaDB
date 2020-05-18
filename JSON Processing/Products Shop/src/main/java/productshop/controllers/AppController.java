package productshop.controllers;

import productshop.services.CategoryService;
import productshop.services.ProductService;
import productshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class AppController implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;

    @Autowired
    public AppController(UserService userService, CategoryService categoryService,
                         ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userService.seedUsers();
        this.categoryService.seedCategories();
        this.productService.seedProducts();

       // this.productService.getProductsInRange("500", "1000");
       // this.userService.getUsersWithSoldItems();

        //this.categoryService.findAllCategories();

       //this.userService.getUserWithSoldItemsOrdered();
    }
}
