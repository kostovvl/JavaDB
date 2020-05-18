package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.constants.FileLocations;
import project.entities.Author;
import project.entities.Book;
import project.repositories.AuthorRepository;
import project.services.interfaces.AuthorService;
import project.utils.FileUtil;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImp implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public long getAuthorsSize() {
        return this.authorRepository.count();
    }

    @Override
    public Author getAuthorById(int id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public void seedAuthors() throws IOException {

        String [] fileData = this.fileUtil.readFile(FileLocations.AUTHORS_PATH);

        for (String fileDatum : fileData) {
            String[] row = fileDatum.split(" ");
            if (this.authorRepository.findByFirstNameAndLastName(row[0], row[1]) == null) {
                Author author = new Author(row[0], row[1]);
                this.authorRepository.saveAndFlush(author);
            }
        }
    }


    @Override
    public List<Author> findAutorsWithFirstNameEndingWith(String ending) {
        return this.authorRepository.findByFirstNameEndingWith(ending);
    }

    @Override
    public List<Author> findAuthorsAndOrderByBookCount() {
        return this.authorRepository.findAll()
                .stream().sorted((a, b) -> {
                    int result = 0;
                    result = b.getBooks().stream().map(Book::getCopies).reduce(0, Integer::sum)
                            .compareTo(a.getBooks().stream().map(Book::getCopies).reduce(0, Integer::sum));
                    return result;
                }).collect(Collectors.toList());
    }


}
