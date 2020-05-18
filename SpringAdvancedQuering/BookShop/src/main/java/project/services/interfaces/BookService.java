package project.services.interfaces;

import project.entities.Book;
import project.entities.EditionType;
import project.entities.Restriction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface BookService {

    void seedBooks() throws IOException;

    List<Book> findAllByAgeRestriction(Restriction restriction);

    List<Book> findAllByEditionType();

    List<Book> findAllExcludingAPriceRange();

    List<Book> findAllExceptReleasedInTheYearOf(String year);

    List<Book> findAllReleasedBefore(String date);

    List<Book> findAllContaining(String pattern);

    List<Book> findAllByAuthorLastNameStarting(String pattern);

    int countOfBooksWithTitleLongerThan(int length);


    Book getReducedBook(String title);

    int removeBooks(int minimumCopies);

}
