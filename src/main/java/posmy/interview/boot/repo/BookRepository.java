package posmy.interview.boot.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, String> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByBorrower(String borrower);
}
