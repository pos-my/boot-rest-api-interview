package posmy.interview.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;

/**
 * @author Hafiz
 * @version 0.01
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
	private BookRepository bookRepository;
	
	@Override
	public Book saveBook(Book Book) throws Exception {
		return bookRepository.save(Book);
	}

	@Override
	public List<Book> findAllBooks() {
		return (List<Book>)bookRepository.findAll();
	}

	@Override
	public Book updateBook(Book Book) throws Exception {
		return saveBook(Book);
	}

	@Override
	public void deleteBook(Book Book) throws Exception {
		bookRepository.delete(Book);
	}

	@Override
	public Book findByBookCode(String bookCode) {
		return bookRepository.findByBookCode(bookCode);
	}

	@Override
	public Book deleteByBookCode(String bookCode) throws Exception {
		return bookRepository.deleteByBookCode(bookCode);
	}
}
