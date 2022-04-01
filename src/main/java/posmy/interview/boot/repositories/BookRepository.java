package posmy.interview.boot.repositories;

import posmy.interview.boot.entities.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatus(String status);

    Book findByIsnb(String Isnb);

}