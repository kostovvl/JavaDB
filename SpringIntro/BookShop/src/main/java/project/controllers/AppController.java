package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import project.entities.Author;
import project.entities.Book;
import project.services.interfaces.AuthorService;
import project.services.interfaces.BookService;
import project.services.interfaces.CategoryService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Scanner;

@Controller
@Transactional
public class AppController implements CommandLineRunner {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;
    private Scanner scanner;

    @Autowired
    public AppController(AuthorService authorService, BookService bookService, CategoryService categoryService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.scanner = new Scanner(System.in);
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
                String answer = scanner.nextLine();
                if (answer.toLowerCase().equals("y")) {
                    break;
                }
            }

            switch (command) {
                case "1": {
                    System.out.println("Task 1:\nChoose the release year, after");
                    String year = scanner.nextLine();

                    List<Book> books = this.bookService.getBooksReleasedAfter(year);
                    if (books.isEmpty()) {
                        System.out.println("No books released after year" + year + "!");
                    } else {
                        books.forEach(b -> System.out.println(b.getTitle()));
                    }
                    System.out.println("End of Task 1\n**********************");
                    break;
                }
                case "2": {
                    System.out.println("Task 2:\nChoose the release year, before");
                    String year = scanner.nextLine();
                    List<Author> authors = this.authorService.getAllAuthorsWithBooksBeforeYear(year);

                    if (authors.isEmpty()) {
                        System.out.println("No authors with books released before year" + year + "!");
                    } else {
                        authors.stream()
                                .forEach(a -> {
                                    StringBuilder books = new StringBuilder();
                                    a.getBooks().stream()
                                            .forEach(book ->
                                                    books.append(book.getTitle() + " "
                                                    + book.getReleaseDate() + System.lineSeparator()));
                                    System.out.printf("Author: %s %s%n" +
                                            "Books:%n" +
                                            "%s%n", a.getFirstName(), a.getLastName(), books);
                                });
                    }
                    System.out.println("End of Task 2\n**********************");
                    break;
                }
                case "3": {
                    System.out.println("Task 3: ");

                    List<Author> authors = this.authorService.getAuthorsAndOrderByTheSizeOfTheirBook();
                    authors.forEach(a -> System.out.printf("Author: %s %s, Book count: %d%n", a.getFirstName(), a.getLastName(),
                            a.getBooks().size()));
                    System.out.println("End of Task 3\n**********************");
                    break;
                }
                case "4": {
                    System.out.println("Task 4: ");

                    this.bookService.findAllBooksOfAuthorGP().stream()
                            .forEach(b -> System.out.printf("Title: %s, Release date: %s, Copies: %d%n", b.getTitle(),
                                    b.getReleaseDate(), b.getCopies()));
                    System.out.println("End of Task 4\n**********************");
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
