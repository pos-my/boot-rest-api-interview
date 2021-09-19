package posmy.interview.boot.service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.service.BookService;


@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
    
    @MockBean
	private BookService bookService;
    
  
	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testGetAllBook() throws Exception
	{
		Book book = Book.builder().bookId(12345L).isbn("12345").title("testBook").available("AVAILABLE").build();
		when(bookService.getAllBooks()).thenReturn(List.of(book));
		  
		this.mockMvc.perform(get("/lib/book").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testAddBook()  throws Exception
	{
		Book book = Book.builder().bookId(12345L).isbn("12345").title("testBook").build();
		when(bookService.addBook(any(Book.class))).thenReturn(book);
		BookDto bookDto = new BookDto(book);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(bookDto);
		mockMvc.perform(post("/lib/book")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(jsonStr)						
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isCreated())			   
			   .andExpect(header().string("Location", "http://localhost/lib/book/12345"));	   	
	}
	
	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testUpdateBook()  throws Exception
	{
		Book book = Book.builder().bookId(1L).isbn("12345").title("testBook Old").build();
		BookDto bookDto = new BookDto(book);
		when(bookService.updateBook(any(Book.class))).thenReturn(book);
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(bookDto);
		
		
		mockMvc.perform(put("/lib/book")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(jsonStr)						
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())			   
			   .andExpect(jsonPath("$.title").value("testBook Old"));			   	
	}
	
	@Test
	@WithMockUser(username = "test", roles={"LIBRARIAN"})
	public void testDeleteBook()  throws Exception
	{
		Book book = Book.builder().bookId(1L).isbn("12345").title("testBook Old").build();
	    mockMvc.perform(delete("/lib/book/{bookId}", book.getBookId())
		   		.contentType(MediaType.APPLICATION_JSON)					
		   		.accept(MediaType.APPLICATION_JSON))
	    		.andExpect(status().isNoContent());	
	    verify(bookService, times(1)).deleteBook(book.getBookId());
	    verifyNoMoreInteractions(bookService);
	}
	
	@Test
	@WithMockUser(username = "test", roles={"MEMBER"})
	public void testGetAvailableBook()  throws Exception
	{
		Book book = Book.builder().bookId(12345L).isbn("12345").title("testBook").available("AVAILABLE").build();
		when(bookService.getAllAvailableBooks()).thenReturn(List.of(book));
		  
		this.mockMvc.perform(get("/mem/book/available").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "test2", roles={"MEMBER"})
	public void testReturnBook()  throws Exception
	{
		Book book = Book.builder().bookId(1L).isbn("12345").title("testBook Old").available("BORROWED").build();
		Book book2 = Book.builder().bookId(1L).isbn("12345").title("testBook Old").available("AVAILABLE").build();
        when(bookService.returnBook(book.getBookId())).thenReturn(book2);

		mockMvc.perform(put("/mem/book/return/{bookId}", book.getBookId())
			   .contentType(MediaType.APPLICATION_JSON)					
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk())			   
			   .andExpect(jsonPath("$.available").value("AVAILABLE"));			   	
	}
	
	@Test
	@WithMockUser(username = "test2", roles={"MEMBER"})
	public void testBorrowBook()  throws Exception
	{
		Book book = Book.builder().bookId(1L).isbn("12345").title("testBook Old").available("BORROWED").build();
        when(bookService.borrowBook(anyLong(),anyString())).thenReturn(book);

		mockMvc.perform(put("/mem/book/borrow/{bookId}", book.getBookId())
			   .contentType(MediaType.APPLICATION_JSON)				
			   .accept(MediaType.APPLICATION_JSON))
			   .andExpect(status().isOk());
		verify(bookService, times(1)).borrowBook(anyLong(),anyString());
	}

}
