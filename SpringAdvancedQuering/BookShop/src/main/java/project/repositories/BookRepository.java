package project.repositories;

import org.springframework.context.annotation.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.entities.Book;
import project.entities.EditionType;
import project.entities.Restriction;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByTitle(String title);

    List<Book> findAllByRestriction(Restriction restriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType edition, int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal low, BigDecimal high);

    List<Book> findAllByReleaseDateIsLessThanOrReleaseDateGreaterThan(LocalDate low, LocalDate high);

    List<Book> findAllByReleaseDateIsLessThan(LocalDate low);

    List<Book> findAllByTitleContaining(String string);

    List<Book> findAllByAuthorLastNameStartsWith(String pattern);

    @Query("select count(b) from Book as b where length(b.title) >:length ")
    int countBooksByTitleLength(int length);


    @Query("select count(b) from Book as b where b.copies <:minimumCopies")
    int countBooksByCopiesSize(int minimumCopies);

    @Transactional
    @Modifying
    @Query("delete from Book as b where b.copies <:minimumCopies")
    void removeBooksByCopiesSize(int minimumCopies);


}
