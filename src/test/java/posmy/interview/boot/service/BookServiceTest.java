package posmy.interview.boot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

import posmy.interview.boot.constant.BookStatus;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.rest.BookRequest;
import posmy.interview.boot.model.rest.BookResponse;
import posmy.interview.boot.model.rest.GetBookRequest;
import posmy.interview.boot.model.rest.GetBookResponse;
import posmy.interview.boot.respository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	
	@Mock
	private BookRepository bookRepository;
	
	
	private BookService underTest;
	
	@BeforeEach
	void setUp() {
		
		underTest = new BookServiceImpl(bookRepository);
	}
	
	@Test
	void checkIfGetAllBooksSuccess() {
		//given
		//Initialize Entity
		Book book = new Book();
		book.setBookId(Integer.toUnsignedLong(1));
		book.setBookName("Harry Potter");
		book.setStatus("AVAILABLE");
		
		Book book1 = new Book();
		book.setBookId(Integer.toUnsignedLong(1));
		book.setBookName("Robots");
		book.setStatus("AVAILABLE");
		
		//Add book to list
		List<Book> bookList = new ArrayList<>();
		bookList.add(book);
		bookList.add(book1);
		
		//Give them knowledge of return list
		when(bookRepository.findAll()).thenReturn(bookList);
		
		//when
		GetBookResponse finalResponse = underTest.getAllBooks();
		
		//then
		assertThat(finalResponse).isNotNull();
		
	}
	
	@Test
	void checkIfGetBookByBookIdSuccess() throws Exception {
		//given
		//Initialize Rest request
		BookRequest request = new BookRequest();
		request.setId(Integer.toUnsignedLong(1));
		
		//Initialize Entity
		Book book = new Book();
		book.setBookId(Integer.toUnsignedLong(1));
		book.setBookName("Harry Potter");
		book.setStatus("AVAILABLE");
		
		when(bookRepository.findByBookId(any(long.class))).thenReturn(book);
		
		//when 
		BookResponse finalResponse = underTest.getBooksById(request);
		
		//then 
		assertThat(finalResponse).isNotNull();
		
	}
	

	@Test 
	void checkIfAddBooksSucess() throws Exception {
		//given
		//Initialize Rest request
		BookRequest request = new BookRequest();
		request.setId(Integer.toUnsignedLong(1));
		request.setBookName("Harry Potter");
		request.setStatus(BookStatus.AVAILABLE.name());
		
		//Initialize Entity
		Book book = new Book();
		book.setBookId(request.getId());
		book.setBookName(request.getBookName());
		book.setStatus(request.getStatus());
		
		BookResponse response = new BookResponse();
		
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		
		//when
		response = underTest.addBooks(request);
		
		//then 
		assertThat(response).isNotNull();
		
	}
	
	@Test
	void checkIfUpdateBooksSuccess() throws Exception {
		//given
		//Initialize Rest Request
		BookRequest request = new BookRequest();
		request.setId(Integer.toUnsignedLong(1));
		request.setBookName("Harry Potter");
		request.setStatus(BookStatus.BORROWED.name());
		
		//Book before update
		Book bookBefore = new Book();
		bookBefore.setBookId(Integer.toUnsignedLong(1));
		bookBefore.setBookName("Harry Potters");
		bookBefore.setStatus(BookStatus.AVAILABLE.name());
		
		//Initialize Entity
		Book bookAfter = new Book();
		bookAfter.setBookId(request.getId());
		bookAfter.setBookName(request.getBookName());
		bookAfter.setStatus(request.getStatus());
		
		BookResponse response = new BookResponse();
		
		when(bookRepository.findByBookId(any(long.class))).thenReturn(bookBefore);
		when(bookRepository.save(any(Book.class))).thenReturn(bookAfter);
		
		//when
		response = underTest.updateBooks(request);
		
		//then
		assertThat(response.getBookDetail().getBookId()).isEqualTo(request.getId());
	}
	
	@Test
	void checkIfDeleteBooksSuccess() throws Exception {

		//given
		//Initialize Rest Request
		GetBookRequest request = new GetBookRequest();
		request.setId(Integer.toUnsignedLong(1));
		
		//Initialize Entity
		Book book = new Book();
		book.setBookId(request.getId());
		book.setBookName("Harry Potter");
		book.setStatus(BookStatus.AVAILABLE.name());
		
		when(bookRepository.findByBookId(any(long.class))).thenReturn(book);
		
		//when
		underTest.removeBooks(request);
		
		//then
		//Capture the argument of delete()
		verify(bookRepository, times(1)).deleteById(book.getBookId());
	}

	@Test
	void checkIfBorrowBooksChangeToBorrowStatus() throws Exception {
		//give
		//Initialize Rest Request
		BookRequest request = new BookRequest();
		request.setId(Integer.toUnsignedLong(1));
		
		//Initialize Entity
		Book book = new Book();
		book.setBookId(request.getId());
		book.setBookName("Harry Potter");
		
		when(bookRepository.findByBookId(any(long.class))).thenReturn(book);
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		
		//when
		BookResponse response = underTest.borrowBooks(request);
		
		//then
		assertThat(response.getBookDetail().getStatus()).isEqualTo(BookStatus.BORROWED.name());
	}
	
	@Test
	void checkIfReturnBooksChangeToAvailableStatus() throws Exception {
		//give
		//Initialize Rest Request
		BookRequest request = new BookRequest();
		request.setId(Integer.toUnsignedLong(1));
		
		//Initialize Entity
		Book book = new Book();
		book.setBookId(request.getId());
		book.setBookName("Harry Potter");
		
		when(bookRepository.findByBookId(request.getId())).thenReturn(book);
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		
		//when
		BookResponse response = underTest.returnBooks(request);
		
		//then
		assertThat(response.getBookDetail().getStatus()).isEqualTo(BookStatus.AVAILABLE.name());
	}
}
