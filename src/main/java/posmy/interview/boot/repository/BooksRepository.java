package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.Books;

public interface BooksRepository extends JpaRepository<Books, Long> {
}
