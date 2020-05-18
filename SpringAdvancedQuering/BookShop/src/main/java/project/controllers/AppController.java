package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.w3c.dom.ls.LSOutput;
import project.entities.Book;
import project.entities.Restriction;
import project.services.interfaces.AuthorService;
import project.services.interfaces.BookService;
import project.services.interfaces.CategoryService;

import javax.transaction.Transactional;
import java.util.Scanner;

@Controller
@Transactional
public class AppController implements CommandLineRunner {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private final Scanner scanner;

    @Autowired
    public AppController(AuthorService authorService, BookService bookService,
                         CategoryService categoryService, Scanner scanner) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {

        this.categoryService.seedCategory();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        System.out.println("Dear colleague,\n" +
                "welcome to the Spring Intro application.\n" +
                "Select the task you want to test from 1 to 4, if you want to Exit press 0 (Default 0):\n");

        while (true) {
            System.out.println("Choose next action:");
            String command = scanner.nextLine();
            command = command.trim().isEmpty() ? "0" : command;

            if (command.equals("0")) {
                System.out.println("Are you sure tou want to exit? (Y/N):");
                String answer = scanner.nextLine().trim();
                if (answer.toLowerCase().equals("y")) {
                    break;
                }
            }

            switch (command) {
                case "1": {
                    System.out.println("Task 1:\nWrite age restriction: ");
                    String ageRestriction = scanner.nextLine().toUpperCase();
                    if (ageRestriction.equals("MINOR") ||
                            ageRestriction.equals("TEEN") || ageRestriction.equals("ADULT")) {
                        Restriction restriction = Restriction.valueOf(ageRestriction);
                        this.bookService.findAllByAgeRestriction(restriction)
                                .forEach(b -> System.out.printf("%s%n", b.getTitle()));
                    } else {
                        System.out.println("No such age restriction!");
                    }

                    System.out.println("End of Task 1\n**********************");
                    break;
                }
                case "2": {
                    System.out.println("Task 2:");
                    this.bookService.findAllByEditionType()
                            .forEach(b -> System.out.printf("%s%n", b.getTitle()));
                    System.out.println("End of Task 2\n**********************");
                    break;
                }
                case "3": {
                    System.out.println("Task 3: ");
                    this.bookService.findAllExcludingAPriceRange()
                            .forEach(b -> System.out.printf("%s - $%.2f%n",
                                    b.getTitle(), b.getPrice()));
                    System.out.println("End of Task 3\n**********************");
                    break;
                }
                case "4": {
                    System.out.println("Task 4:\nWrite the year of release:");
                    String yearOfRelease = scanner.nextLine();
                    this.bookService.findAllExceptReleasedInTheYearOf(yearOfRelease)
                            .forEach(b -> System.out.printf("%s%n", b.getTitle()));
                    System.out.println("End of Task 4\n**********************");
                    break;
                }
                case "5": {
                    System.out.println("Task 5:\nWrite release date in format (dd-MM-yyyy):");
                    String dateInput = scanner.nextLine();
                    this.bookService.findAllReleasedBefore(dateInput)
                            .forEach(b -> System.out.printf("%s %s %.2f%n",
                                    b.getTitle(), b.getEditionType(), b.getPrice()));

                    System.out.println("End of Task 5\n**********************");
                    break;
                }
                case "6": {
                    System.out.println("Task 6:\nWrite name ending pattern:");
                    String ending = scanner.nextLine();
                    this.authorService.findAutorsWithFirstNameEndingWith(ending)
                            .forEach(a -> System.out.printf("%s %s%n",
                                    a.getFirstName(), a.getLastName()));
                    System.out.println("End of Task 6\n**********************");
                    break;
                }
                case "7": {
                    System.out.println("Task 7:\nWrite the given string:");
                    String pattern = scanner.nextLine();
                    this.bookService.findAllContaining(pattern)
                            .forEach(b -> System.out.printf("%s%n", b.getTitle()));
                    System.out.println("End of Task 7\n**********************");
                    break;
                }
                case "8": {
                    System.out.println("Task 8:\nWrite the given string");
                    String pattern = scanner.nextLine();
                    this.bookService.findAllByAuthorLastNameStarting(pattern)
                            .forEach(b -> System.out.printf("%s (%s %s)%n",
                                    b.getTitle(), b.getAuthor().getFirstName(), b.getAuthor().getLastName()));
                    System.out.println("End of Task 8\n**********************");
                    break;
                }
                case "9": {
                    System.out.println("Task 9:\nWrite length of the title: ");
                    int length = Integer.parseInt(scanner.nextLine());
                    System.out.println(this.bookService.countOfBooksWithTitleLongerThan(length));
                    break;
                }
                case "10": {
                    System.out.println("Task 10:\nWrite author's first and last names:");
                    this.authorService.findAuthorsAndOrderByBookCount()
                            .forEach(a -> System.out.printf("%s %s - %d%n",
                                    a.getFirstName(), a.getLastName(),
                                    a.getBooks().stream().map(Book::getCopies).reduce(0, Integer::sum)));
                    System.out.println("End of Task 10\n**********************");
                    break;
                }
                case "11": {
                    System.out.println("Task 11:\nWrite the title of the book:");
                    String title = scanner.nextLine().trim();
                    Book result = bookService.getReducedBook(title);
                    System.out.printf("%s %s %s %.2f%n",
                            result.getTitle(), result.getEditionType(), result.getRestriction(), result.getPrice());
                    System.out.println("End of Task 11\n**********************");
                    break;
                }
                case "12": {
                    System.out.println("Not implemented!");
                    break;
                }
                case "13": {
                    System.out.println("Task 13:\nEnter the minimum number of copies:");
                    int minimumCopies = Integer.parseInt(scanner.nextLine());
                    System.out.println(this.bookService.removeBooks(minimumCopies));
                    System.out.println("End of Task 13\n**********************");
                    break;
                }
                case "14": {
                    System.out.println("Not implemented!");
                    break;
                }
                default: {
                    System.out.println("Wrong selection");
                    break;
                }
            }

        }
        System.out.println("You have exited the program!");
    }


}
