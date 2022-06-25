package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.model.Book;

/**
 * @author Hafiz
 * @version 0.01
 */
public interface BookService {

    List<Book> findAllBooks();
    Book findByBookCode(String bookCode);
    Book saveBook(Book book) throws Exception;
    Book updateBook(Book book) throws Exception;
    Book deleteByBookCode(String bookCode) throws Exception;
	void deleteBook(Book book) throws Exception;
}
