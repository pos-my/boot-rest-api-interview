package posmy.interview.boot.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import posmy.interview.boot.entity.Book;


@Repository
public interface BookDao extends CrudRepository<Book, Long> {

	Optional<Book> findById(Long Id);
	Optional<Book> findByBookCode(String code);
	Optional<Book> findByBookName(String name);
	Boolean existsByBookCode(String bookCode);
	Boolean existsByBookName(String bookName);
}
