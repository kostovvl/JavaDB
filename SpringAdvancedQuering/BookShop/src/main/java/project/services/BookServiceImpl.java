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

    @Override
    public List<Book> findAllByAgeRestriction(Restriction restriction) {
        return this.bookRepository.findAllByRestriction(restriction);
    }

    @Override
    public List<Book> findAllByEditionType() {
        final EditionType golden = EditionType.valueOf("GOLD");
        return this.bookRepository.findAllByEditionTypeAndCopiesLessThan(golden, 5000);
    }

    @Override
    public List<Book> findAllExcludingAPriceRange() {
       final BigDecimal low = new BigDecimal("5");
       final BigDecimal high = new BigDecimal("40");
        return this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(low, high);
    }

    @Override
    public List<Book> findAllExceptReleasedInTheYearOf(String year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate low = LocalDate.parse("01/01/"+year, formatter);
        LocalDate high = LocalDate.parse("31/12/"+year, formatter);
        return this.bookRepository.findAllByReleaseDateIsLessThanOrReleaseDateGreaterThan(low, high);
    }

    @Override
    public List<Book> findAllReleasedBefore(String date) {
        LocalDate releaseDate = generateLocalDate(date);
        return this.bookRepository.findAllByReleaseDateIsLessThan(releaseDate);
    }

    private LocalDate generateLocalDate(String date) {
        String[] dateArr = date.split("-");
        String dateString = dateArr[0] + "/" + dateArr[1] + "/" + dateArr[2];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    @Override
    public List<Book> findAllContaining(String pattern) {
        return this.bookRepository.findAllByTitleContaining(pattern);
    }

    @Override
    public List<Book> findAllByAuthorLastNameStarting(String pattern) {
        return this.bookRepository.findAllByAuthorLastNameStartsWith(pattern);
    }

    @Override
    public int countOfBooksWithTitleLongerThan(int length) {
        return this.bookRepository.countBooksByTitleLength(length);
    }


    @Override
    public Book getReducedBook(String title) {
        Book queried = this.bookRepository.findByTitle(title);
        Book toBePrinted = new Book();
        toBePrinted.setTitle(queried.getTitle());
        toBePrinted.setEditionType(queried.getEditionType());
        toBePrinted.setRestriction(queried.getRestriction());
        toBePrinted.setPrice(queried.getPrice());
        return toBePrinted;
    }

    @Override
    public int removeBooks(int minimumCopies) {
        int result = this.bookRepository.countBooksByCopiesSize(minimumCopies);
        this.bookRepository.removeBooksByCopiesSize(minimumCopies);
        return result;
    }

}
