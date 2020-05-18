package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findByFirstNameAndLastName(String firstName, String LastName);

    List<Author> findAll();

    @Query("select a from Author as a order by a.books.size desc")
    List<Author> getAllAndOrderByBooks();
}
