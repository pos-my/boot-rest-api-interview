package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.exception.CustomNoDataFoundException;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Book.BookStatus;
import posmy.interview.boot.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<BookDto> getAllBooks() {

		List<Book> books = bookRepository.findAll();

		List<BookDto> bookDtos = new ArrayList<>();
		books.forEach(book -> {
			BookDto dto = BookDto.builder().id(book.getId()).code(book.getCode()).title(book.getTitle())
					.status(book.getStatus()).build();
			bookDtos.add(dto);
		});

		return bookDtos;
	}

	public BookDto getBookById(Long id) {
		Book book = getBook(id);
		BookDto dto = BookDto.builder().id(book.getId()).code(book.getCode()).title(book.getTitle())
				.status(book.getStatus()).build();
		
		return dto;
	}
	
	public void saveBook(BookDto bookDto) {
		
		Book book = Book.builder().id(bookDto.getId()).code(bookDto.getCode()).title(bookDto.getTitle())
				.status(bookDto.getStatus()).build();
		
		bookRepository.save(book);
	}
	
	public void updateBook(BookDto bookDto) {
		
		getBook(bookDto.getId());
		
		saveBook(bookDto);
	}
	
	public void deleteBookById(Long id) {
		
		getBook(id);
		
		bookRepository.deleteById(id);
	}
	
	public void borrowBook(Long id) {
		
		Book book = getBook(id);
		
		if (BookStatus.AVAILABLE.equals(book.getStatus())) {
			book.setStatus(BookStatus.BORROWED);
			bookRepository.save(book);
		}
	}
	
	public void returnBook(Long id) {
		
		Book book = getBook(id);
		
		if (BookStatus.BORROWED.equals(book.getStatus())) {
			book.setStatus(BookStatus.AVAILABLE);
			bookRepository.save(book);
		}
		
	}
	
	private Book getBook(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new CustomNoDataFoundException("No book found."));
	}

}
