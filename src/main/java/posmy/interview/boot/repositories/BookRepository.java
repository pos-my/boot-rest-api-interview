package posmy.interview.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByPublishedYear(String publihsedYear);

    List<Book> findByStatus(BookStatus status);

    Optional<Book> findByTitleAndAuthor(String title, String author);
}
