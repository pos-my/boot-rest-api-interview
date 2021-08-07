package posmy.interview.boot.service;

import java.util.List;
import java.util.Optional;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.BookObject;
import posmy.interview.boot.object.UserObject;

public interface IBookService {
	Optional<Book> create(BookObject bookObject);
	
	Optional<Book> update(BookObject bookObject, Long bookId);
	
	Optional<Boolean> delete(Long bookId);
	
	List<Book> findAll();
	
	Optional<Book> findByBookId(Long bookId);

	Optional<Book> borrow(Long bookId);

	Optional<Book> returnBook(Long bookId);
}
