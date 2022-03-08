package posmy.interview.boot.repository;

import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.model.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
}
