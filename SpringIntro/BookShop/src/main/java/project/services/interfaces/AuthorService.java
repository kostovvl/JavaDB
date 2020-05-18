package project.services.interfaces;

import project.entities.Author;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public interface AuthorService {

    long getAuthorsSize();

    void seedAuthors() throws IOException;

    Author getAuthorById(int id);

    List<Author> getAllAuthorsWithBooksBeforeYear(String year);

    List<Author> getAuthorsAndOrderByTheSizeOfTheirBook();
}
