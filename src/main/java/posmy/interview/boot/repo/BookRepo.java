package posmy.interview.boot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.Book;

import java.util.Optional;


public interface BookRepo extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    void deleteByIsbn(String isbn);
}
