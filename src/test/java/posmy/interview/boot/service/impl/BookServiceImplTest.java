package posmy.interview.boot.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import posmy.interview.boot.exception.CustomException;
import posmy.interview.boot.exception.ResourceAlreadyExists;
import posmy.interview.boot.exception.ResourceNotFoundException;
import posmy.interview.boot.model.AccStatus;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.BookService;

@SpringBootTest
public class BookServiceImplTest {
	
	@Autowired
	private BookService bookService;
	
	@MockBean
    private BookRepository bookRepository;
	
	@MockBean
    private UserRepository userRepository;
	
    @Test
    public void getAllBookTest() {
	    Book book1 = Book.builder().bookId(1L).isbn("1234567").title("testBook3").available("AVAILABLE").build();
	    Book book2 = Book.builder().bookId(2L).isbn("12345").title("testBook1").available("AVAILABLE").build();
	    Book book3 = Book.builder().bookId(3L).isbn("123456").title("testBook2").available("AVAILABLE").build();
	    List<Book> bookList = new ArrayList<Book>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        
    	when(bookRepository.findAll()).thenReturn(bookList);
    	List<Book> bList = bookService.getAllBooks();
    	assertEquals(3, bList.size());
    }

	@Test
	public void getAvailableBookTest()  throws Exception
	{
	    Book book1 = Book.builder().bookId(1L).isbn("1234567").title("testBook3").available("AVAILABLE").build();
	    Book book2 = Book.builder().bookId(2L).isbn("12345").title("testBook1").available("AVAILABLE").build();
	    Book book3 = Book.builder().bookId(3L).isbn("123456").title("testBook2").available("AVAILABLE").build();
	    List<Book> bookList = new ArrayList<Book>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
		when(bookService.getAllAvailableBooks()).thenReturn(bookList);
		  
		List<Book> bList = bookService.getAllAvailableBooks();
    	assertEquals(3, bList.size());
	}

    @Test
    public void addBookTest() {
	    Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
	    when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
		when(bookRepository.save(any(Book.class))).thenReturn(book1);
    	Book bookCreated = bookService.addBook(book1);
    	assertEquals(bookCreated.getIsbn(),book1.getIsbn());
    }
    
    @Test
    public void addBookTest_ResourceExist() {
	    Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
	    when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(book1));
    	assertThrows(ResourceAlreadyExists.class, () -> {
    		Book bookCreated = bookService.addBook(book1);
    	 });
    	
    }
    
    @Test
    public void updateBookTest() {
	    Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
	    Book book2 = Book.builder().bookId(1L).isbn("12345").title("testBook New").available("AVAILABLE").build();
	    when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(book1));
		when(bookRepository.save(any(Book.class))).thenReturn(book2);
    	Book bookupdated = bookService.updateBook(book1);
    	assertNotEquals(bookupdated.getTitle(),book1.getTitle());
    }

    @Test
    public void updateBookTest_ResourceNotFound() {
	    Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
	    when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
    	assertThrows(ResourceNotFoundException.class, () -> {
    		Book bookupdated = bookService.updateBook(book1);
    	 });
    }
    
    @Test
    public void borrowBookTest() {
    	Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
    	User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	when(bookRepository.findById(book1.getBookId())).thenReturn(Optional.of(book1));
    	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
    	when(bookRepository.save(any(Book.class))).thenReturn(book1);
    	Book bookBorrowed = bookService.borrowBook(book1.getBookId(),user1.getUsername());
    	assertNotEquals(bookBorrowed.getAvailable(),"AVAILABLE");
    }
    
    @Test
    public void borrowBookTest_borrowedException() {
    	Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("BORROWED").build();
    	User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	when(bookRepository.findById(book1.getBookId())).thenReturn(Optional.of(book1));
    	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
    	when(bookRepository.save(any(Book.class))).thenReturn(book1);
    	assertThrows(CustomException.class, () -> {
    		Book bookBorrowed = bookService.borrowBook(book1.getBookId(),user1.getUsername());
    	 });
    }
    
    @Test
    public void borrowBookTest_ResourceNotFoundException() {
    	Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("BORROWED").build();
    	User user1 = User.builder().userid(1L).username("test").password("password").role(Role.MEMBER).accStatus(AccStatus.ACTIVE).build();
    	when(bookRepository.findById(book1.getBookId())).thenReturn(Optional.empty());
    	when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
    	when(bookRepository.save(any(Book.class))).thenReturn(book1);
    	assertThrows(CustomException.class, () -> {
    		Book bookBorrowed = bookService.borrowBook(book1.getBookId(),user1.getUsername());
    	 });
    }
    
    
    @Test
    public void returnBookTest_InvalidException() {
    	Book book4 = Book.builder().bookId(10L).isbn("12345").title("testBook").available("AVAILABLE").build();
    	when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book4));
    	when(bookRepository.save(any(Book.class))).thenReturn(book4);
    	assertThrows(CustomException.class, () -> {
    		Book bookBorrowed = bookService.returnBook(book4.getBookId());
    	 });
   
    }
    
    @Test
    public void returnBookTest_ResourceNotFoundException() {
    	Book book4 = Book.builder().bookId(10L).isbn("12345").title("testBook").available("AVAILABLE").build();
    	when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
    	assertThrows(ResourceNotFoundException.class, () -> {
    		Book bookBorrowed = bookService.returnBook(book4.getBookId());
    	 });
    }
    
    @Test
    public void returnBookTest() {
    	Book book4 = Book.builder().bookId(10L).isbn("12345").title("testBook").available("BORROWED").build();
    	when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book4));
    	when(bookRepository.save(any(Book.class))).thenReturn(book4);
    	Book bookDto = bookService.returnBook(book4.getBookId());
    	assertNotEquals(bookDto.getAvailable(),"BORROWED");
    }
    
    
    @Test
    public void deleteBookTest() {
    	Book book1 = Book.builder().bookId(1L).isbn("12345").title("testBook").available("AVAILABLE").build();
	    //when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book1));
    	bookService.deleteBook(book1.getBookId());
    	verify(bookRepository).deleteById(book1.getBookId());

    }


}
