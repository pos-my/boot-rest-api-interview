package posmy.interview.boot.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}
