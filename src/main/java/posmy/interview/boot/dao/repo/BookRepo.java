package posmy.interview.boot.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.dao.Book;

public interface BookRepo extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByYear(String year);
    List<Book> findByBorrowedByIgnoreCase(String borrowedBy);
}