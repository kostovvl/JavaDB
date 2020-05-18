package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.constants.FileLocations;
import project.entities.*;
import project.repositories.BookRepository;
import project.services.interfaces.AuthorService;
import project.services.interfaces.BookService;
import project.services.interfaces.CategoryService;
import project.utils.FileUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService,
                           CategoryService categoryService, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.fileUtil = fileUtil;

    }

    @Override
    public void seedBooks() throws IOException {

        String[] fileData = this.fileUtil.readFile(FileLocations.BOOKS_PATH);

        for (String fileDatum : fileData) {
            String[] row = fileDatum.split(" ");
            String title = createTitle(row);

            if(this.bookRepository.findByTitle(title) != null) {
                continue;
            }

            Author author = getRandomAuthor();
            EditionType editionType = EditionType.values()[Integer.parseInt(row[0])];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate releaseDate = LocalDate.parse(row[1], formatter);
            int copies = Integer.parseInt(row[2]);
            BigDecimal price = new BigDecimal(row[3]);
            Restriction restriction = Restriction.values()[Integer.parseInt(row[4])];
            Set<Category> categories = createRandomCategories();
            Book book = new Book(author, editionType, releaseDate,
                    copies, price, restriction, title, categories);

            this.bookRepository.saveAndFlush(book);
        }
    }

    @Override
    public List<Book> getBooksReleasedAfter(String year) {
        String date = "01/01/"+year;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate releasedDate = LocalDate.parse(date, formatter);
        return this.bookRepository.getBooksByReleaseDateAfter(releasedDate);
    }


    private Set<Category> createRandomCategories() {
        Set<Category> result = new HashSet<>();
        Random random = new Random();
        int numberOfCategories = random.nextInt(3) + 1;

        for (int i = 1; i <= numberOfCategories; i++) {
            int categoryId = random.nextInt(8) + 1;
            result.add(this.categoryService.findCategoryByID(categoryId));
        }

        return result;
    }

    @Override
    public List<Book> findAllBooksOfAuthorGP() {
        return this.bookRepository.findAllByGP();
    }

    private String createTitle(String[] row) {
        StringBuilder title = new StringBuilder();

        for (int i = 5; i < row.length; i++) {
            title.append(row[i] + " ");
        }
        return title.toString().trim();
    }

    private Author getRandomAuthor() {
        Random random = new Random();
        int authorId = random.nextInt((int)this.authorService.getAuthorsSize()) + 1;
        return this.authorService.getAuthorById(authorId);
    }
}
