package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.Book;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByTitle(String title);

    List<Book> getBooksByReleaseDateAfter(LocalDate localDate);

    @Query("select b from Book as b where b.author.firstName = 'George'" +
            "and b.author.lastName = 'Powell'" +
            "order by b.releaseDate desc , b.title asc")
    List<Book> findAllByGP();
}
