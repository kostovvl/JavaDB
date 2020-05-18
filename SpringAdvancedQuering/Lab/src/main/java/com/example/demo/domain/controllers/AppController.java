package com.example.demo.domain.controllers;

import com.example.demo.domain.entities.Size;
import com.example.demo.domain.servces.interfaces.IngredientService;
import com.example.demo.domain.servces.interfaces.LabelService;
import com.example.demo.domain.servces.interfaces.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class AppController implements CommandLineRunner {

    private final IngredientService ingredientService;
    private final LabelService labelService;
    private final ShampooService shampooService;
    private final Scanner scanner;

    @Autowired
    public AppController(IngredientService ingredientService, LabelService labelService,
                         ShampooService shampooService, Scanner scanner) {
        this.ingredientService = ingredientService;
        this.labelService = labelService;
        this.shampooService = shampooService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Task: ");
            String taskNumber = scanner.nextLine();

            switch (taskNumber) {
                case "1": {
                    System.out.println("Select size: ");
                    Size size = Size.valueOf(scanner.nextLine().toUpperCase());
                    this.shampooService.findBySize(size)
                            .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                            s.getBrand(), s.getSize(), s.getPrice()));
                 break;
                }
                case "2": {
                    System.out.println("Select size and label: ");
                    Size size = Size.valueOf(scanner.nextLine().toUpperCase());
                    long labelId = Long.parseLong(scanner.nextLine());
                    this.shampooService.findBySizeAndLabelId(size, labelId)
                            .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                                    s.getBrand(), s.getSize(), s.getPrice()));
                    break;
                }
                case "3": {
                    System.out.println("Select price: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());
                    this.shampooService.findByPriceHigherThan(price)
                            .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                                    s.getBrand(), s.getSize(), s.getPrice()));
                    break;
                }
                case "4": {
                    System.out.println("Select starting model: ");
                    String model = scanner.nextLine();
                    this.ingredientService.findByNameStartingWith(model)
                            .forEach(i -> System.out.printf("%s%n", i.getName()));
                    break;
                }
                case "5": {
                    System.out.println("Write ingredients: ");
                    Set<String> ingredients = new HashSet<>();
                    String ingredient;
                    while (!(ingredient = scanner.nextLine()).isEmpty()) {
                        ingredients.add(ingredient);
                    }

                    this.ingredientService.findByContainingIngredients(ingredients)
                            .forEach(i -> System.out.printf("%s%n", i.getName()));
                    break;
                }
                case "6": {
                    System.out.println("Write price: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());
                    System.out.println(this.shampooService.countOfShampoosWithPriceLowerThan(price));
                    break;
                }
                case "7": {
                    System.out.println("Write list: ");
                    System.out.println("Write ingredients: ");
                    Set<String> ingredients = new HashSet<>();
                    String ingredient;
                    while (!(ingredient = scanner.nextLine()).isEmpty()) {
                        ingredients.add(ingredient);
                    }
                    this.shampooService.selectFromListOfIngredients(ingredients)
                            .forEach(s -> System.out.printf("%s%n", s.getBrand()));
                    break;
                }
                case "8": {
                    System.out.println("Write count: ");
                    int count = Integer.parseInt(scanner.nextLine());
                    this.shampooService.selectFromIngredientsCount(count)
                            .forEach(s -> System.out.printf("%s%n", s.getBrand()));
                    break;
                }
                case "9": {

                    break;
                }
                case "10": {
                    System.out.println("Write name: ");
                    String name = scanner.nextLine();
                    this.ingredientService.deleteIngredientByName(name);
                    break;
                }
                case "11": {
                    System.out.println("Write price: ");
                    BigDecimal price = new BigDecimal(scanner.nextLine());
                    this.ingredientService.updatePriceByPrice(price);
                    break;
                }
                case "12": {
                    System.out.println("Write names: ");
                    System.out.println("Write ingredients: ");
                    Set<String> names = new HashSet<>();
                    String ingredient;
                    while (!(ingredient = scanner.nextLine()).isEmpty()) {
                        names.add(ingredient);
                    }
                    this.ingredientService.updatePriceByNameList(names);
                    break;
                }
                default:{
                    return;
                }
            }
        }
    }
}
