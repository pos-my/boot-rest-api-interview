package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.model.Book;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    /**
     * find book by name
     *
     * @param name name
     * @return book
     */
    List<Book> findByName(String name);
}
