package posmy.interview.boot.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import posmy.interview.boot.domain.Book;
import posmy.interview.boot.enums.BookStatus;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, PagingAndSortingRepository<Book, UUID> {

    Page<Book> findBookByBookStatus(BookStatus bookStatus, Pageable pageable);

    Optional<Book> findBookByIsbn(String isbn);
}
