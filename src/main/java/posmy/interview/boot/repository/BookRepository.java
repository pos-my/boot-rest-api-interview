package posmy.interview.boot.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import posmy.interview.boot.model.Book;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface BookRepository extends JpaRepository<Book, UUID> {

	Book findByBookCode(String bookCode);
	Book deleteByBookCode(String bookCode) throws Exception;
}
