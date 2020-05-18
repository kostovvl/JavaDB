package project.services.interfaces;

import project.entities.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface BookService {

    void seedBooks() throws IOException;

    List<Book> getBooksReleasedAfter(String year);

    List<Book> findAllBooksOfAuthorGP();
}
