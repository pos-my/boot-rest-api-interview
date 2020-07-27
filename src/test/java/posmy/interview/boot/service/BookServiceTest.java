package posmy.interview.boot.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.Book.BookStatus;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.service.BookService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {
	
	// class to be tested
	@Autowired
	private BookService bookService;

	@MockBean
	private BookRepository bookRepository;
	
	@Test
	public void givenNoInput_whenGetAllBooks_thenReturnAllBooks() {
		
		Book book1 = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.AVAILABLE).build();
		Book book2 = Book.builder().id(2L).code("CODE2").title("Dummy Book Title 2").status(BookStatus.BORROWED).build();
		
		List<Book> bookList = new ArrayList<>();
		bookList.add(book1);
		bookList.add(book2);
		
		when(bookRepository.findAll()).thenReturn(bookList);
		
		List<BookDto> result = bookService.getAllBooks();
		
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("CODE1", result.get(0).getCode());
		assertEquals("CODE2", result.get(1).getCode());
		assertEquals(BookStatus.AVAILABLE, result.get(0).getStatus());
		assertEquals(BookStatus.BORROWED, result.get(1).getStatus());
	}
	
	@Test
	public void givenBookId_whenGetBookById_thenReturnBook() {
		
		Book book1 = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.AVAILABLE).build();
		
		when(bookRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(book1));
		
		BookDto result = bookService.getBookById(1L);
		
		assertNotNull(result);
		assertEquals("CODE1", result.getCode());
		assertEquals(BookStatus.AVAILABLE, result.getStatus());
	}
	
	@Test
	public void givenBookDto_whenSaveBook_thenAbleToSaveSuccessfully() {
		
		BookDto bookDto = BookDto.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.AVAILABLE).build();
		
		bookService.saveBook(bookDto);
		
		verify(bookRepository, times(1)).save(ArgumentMatchers.any(Book.class));
	}
	
	@Test
	public void givenBookDto_whenBorrowBook_thenAbleToUpdateStatusSuccessfully() {
		
		Book bookBefore = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.AVAILABLE).build();
		Book bookAfter = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.BORROWED).build();
		when(bookRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(bookBefore));
		
		bookService.borrowBook(1L);
		
		verify(bookRepository, times(1)).save(bookAfter);
	}
	
	@Test
	public void givenBookDto_whenReturnBook_thenAbleToUpdateStatusSuccessfully() {
		
		Book bookBefore = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.BORROWED).build();
		Book bookAfter = Book.builder().id(1L).code("CODE1").title("Dummy Book Title 1").status(BookStatus.AVAILABLE).build();
		when(bookRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(bookBefore));
		
		bookService.returnBook(1L);
		
		verify(bookRepository, times(1)).save(bookAfter);
	}
	
}
