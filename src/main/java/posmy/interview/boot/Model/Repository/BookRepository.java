package posmy.interview.boot.Model.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.Model.Book;
public interface BookRepository extends JpaRepository<Book, Long> {



}
