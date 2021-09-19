package posmy.interview.boot.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.exception.CustomException;
import posmy.interview.boot.exception.ResourceAlreadyExists;
import posmy.interview.boot.exception.ResourceNotFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Book updateBook(Book book) {
		Optional<Book> tmpBook = bookRepository.findByIsbn(book.getIsbn());
		Book updatedBook = null;
		if(tmpBook.isPresent()) {
			Book existBook = tmpBook.get();
			existBook.setTitle(book.getTitle());
			existBook.setAvailable(book.getAvailable());
			existBook.setUser(book.getUser());
			updatedBook=bookRepository.save(existBook);
		}else {
			throw new ResourceNotFoundException("book not exists");
		}
		return updatedBook;
	}

	@Override
	public Book addBook(Book book) {
		Optional<Book> tmpBook = bookRepository.findByIsbn(book.getIsbn());
		Book updatedBook = null;
		if(tmpBook.isPresent()) {
			throw new ResourceAlreadyExists("book already exists");
		}else {
			book.setAvailable("AVAILABLE");
			updatedBook=bookRepository.save(book);
		}
		return updatedBook;
	}
	
	@Override
	public List<Book> getAllBooks() {
		List<Book> book = bookRepository.findAll();
		return book;
		 
	}

	@Override
	public List<Book> getAllAvailableBooks() {
		return bookRepository.findByAvailable("AVAILABLE");
	}
	
	@Override
	public Book borrowBook(Long bookid, String username) {
		Optional<Book> bookOpt = bookRepository.findById(bookid);
		Optional<User> userOpt = userRepository.findByUsername(username);
		Book book =null;

		if(userOpt.isPresent() && bookOpt.isPresent()) {
			book=bookOpt.get();
			if(book.getAvailable().equalsIgnoreCase("AVAILABLE")){
				book.setAvailable("BORROWED");
				book.setUser(userOpt.get());
				book = bookRepository.save(book);
			} else {
				throw new CustomException("invalid request");
			}
		} else {
			throw new CustomException("Book or user not exist");
		}
		return book;
		
	}

	@Override
	public Book returnBook(Long bookid) {
		Optional<Book> bookOpt = bookRepository.findById(bookid);
		Book book =null;
		if(bookOpt.isPresent()) {
			book = bookOpt.get();
			if(book.getAvailable().equalsIgnoreCase("BORROWED")) {
				book.setAvailable("AVAILABLE");
				book.setUser(null);
				book = bookRepository.save(book);
			} else {
				throw new CustomException("invalid request");
			}
		} else {
			throw new ResourceNotFoundException("Not a valid book");
		}
		return book;
	}

	@Override
	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}


	

}
