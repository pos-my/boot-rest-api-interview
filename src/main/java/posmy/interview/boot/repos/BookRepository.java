package posmy.interview.boot.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
