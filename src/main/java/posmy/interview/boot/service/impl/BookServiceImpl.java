package posmy.interview.boot.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import posmy.interview.boot.config.TokenProvider;
import posmy.interview.boot.dao.BookDao;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.InvalidUserCredentialsException;
import posmy.interview.boot.model.requestModel.BookRegisterRequest;
import posmy.interview.boot.model.responseModel.MessageResponse;
import posmy.interview.boot.service.BookService;

@Service(value = "bookService")
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDao bookDAO;

	@Autowired
	private TokenProvider jwtTokenUtil;



	public List<Book> findAll() {
		List<Book> list = new ArrayList<>();
		bookDAO.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public Optional<Book> findById(Long id) {
		return bookDAO.findById(id);
	}

	@Override
	public Optional<Book> findByCode(String code) {
		return bookDAO.findByBookCode(code);
	}

	@Override
	public Optional<Book> findByName(String name) {
		return bookDAO.findByBookName(name);
	}

	@Override
	public void saveBook(BookRegisterRequest bookReq) {
		// Create new book
		Book book = new Book(bookReq.getBookCode(), bookReq.getBookName());
		book.setStatus(AVA);

		bookDAO.save(book);
	}

	@Override
	public ResponseEntity<?> borrowBook(Long id, User user) {

		LocalDate currentUtilDate = LocalDate.now();
		Book book = bookDAO.findById(id).orElseThrow(() -> new InvalidUserCredentialsException("Error: Book is not found."));


		if(book.getStatus().equals(AVA)) {
			book.setDateBorrowed(currentUtilDate);
			book.setDateReturned(null);
			book.setStatus(BOR);
			book.setUsers(user);


		}else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Book not available."));
		}

		final Book borrowedBook = bookDAO.save(book);

		return ResponseEntity.ok(borrowedBook);
	}

	@Override
	public ResponseEntity<?> returnBook(Long id, User user) {

		LocalDate currentUtilDate = LocalDate.now();

		Book book = bookDAO.findById(id).orElseThrow(() -> new RuntimeException("Error: Book is not found."));

		if(!book.getStatus().equals(BOR)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Book was not borrowed."));
		}


		List books = user.getBook();

		if(!books.contains(book)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Please return a book that you borrowed."));
		}

		book.setDateReturned(currentUtilDate);
		book.setStatus(AVA);
		book.setUsers(null);

		final Book returnedBook = bookDAO.save(book);

		return ResponseEntity.ok(returnedBook);
	}


}
