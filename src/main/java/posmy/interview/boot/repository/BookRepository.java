package posmy.interview.boot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import posmy.interview.boot.model.Book;  

public interface BookRepository extends JpaRepository<Book, Long>{
	
    List<Book> findByAvailable(String available);

	Optional<Book> findByIsbn(String isbn);
}
