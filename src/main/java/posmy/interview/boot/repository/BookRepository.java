package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByDeleted(boolean deleted);

    Optional<Book> findByIdAndStateAndDeleted(Long id, Book.State state, boolean deleted);

    Optional<Book> findByIdAndDeleted(Long id, boolean deleted);

}