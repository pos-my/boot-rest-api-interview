package posmy.interview.boot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookShelf extends JpaRepository<Book, Long> {
	
	List<Book> findByName(String name);
	List<Book> findById(long id);

}
