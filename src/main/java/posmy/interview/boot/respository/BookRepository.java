package posmy.interview.boot.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	Book findByBookId(long id);
	
}
