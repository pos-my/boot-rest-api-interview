package posmy.interview.boot.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonProcessingException;

import posmy.interview.boot.entities.BookBorrowRecordRepository;
import posmy.interview.boot.entities.BookRepository;
import posmy.interview.boot.entities.MemberRepository;
import posmy.interview.boot.util.DummyUtils;

class BookServiceTest {
	
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;
	
	@Mock
	private BookBorrowRecordRepository bookBorrowRecordRepository;
	
	@Mock
	private MemberRepository memberRepository; 

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		bookService = new BookService(bookRepository, bookBorrowRecordRepository, memberRepository);
	}

	@Test
	void whenGetAllBooks_thenSuccess() throws Exception {
		int offset = 0;
		int limit = 10;

		List<Book> mockResult = new ArrayList<>();
		mockResult.add(DummyUtils.getBook());
		Mockito.when(bookService.getAllBooks()).thenReturn(mockResult);
		
		List<Book> results = bookService.getAllBooks().stream().skip(offset).limit(limit).collect(Collectors.toList());

		assertTrue(Objects.nonNull(results));
		assertEquals(mockResult, results);
	}
	
	@Test
	void whenAddBooks_thenSuccess() throws Exception {
		
		List<Book> addBook = new ArrayList<>();
		addBook.add(DummyUtils.getBook());
		addBook.add(new Book(12379871L, "A book"));
		
		Mockito.when(bookService.addBooks(addBook)).thenReturn(true);
		
		boolean result = bookService.addBooks(addBook);
		assertTrue(result);
	}
}
