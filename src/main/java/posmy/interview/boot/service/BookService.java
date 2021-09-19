package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;

public interface BookService {
	 
	//public Book addBook(Book book); 
	public List<Book> getAllBooks();  
	public Book returnBook(Long bookid);   
	public void deleteBook(Long bookid);
	public List<Book> getAllAvailableBooks();
	public Book borrowBook(Long bookid, String username);
	public Book addBook(Book book);
	public Book updateBook(Book book);
}
