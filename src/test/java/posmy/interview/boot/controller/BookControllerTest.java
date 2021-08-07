package posmy.interview.boot.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.object.BookObject;
import posmy.interview.boot.security.TokenAuthenticationFilter;
import posmy.interview.boot.security.TokenAuthenticationProvider;
import posmy.interview.boot.security.UserAuthenticationService;
import posmy.interview.boot.service.IBookService;

@WebMvcTest(BookController.class)
public class BookControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	IBookService bookService;
	
	@MockBean
	UserAuthenticationService authentication;
	
	@MockBean 
	TokenAuthenticationProvider tokenAuthenticationProvider;
	
	@MockBean
	TokenAuthenticationFilter tokenAuthenticationFilter;
	
    @Autowired
    ObjectMapper mapper;
	
	Role ROLE_1 = new Role(1L, "LIBRARIAN");
	
	User USER_1 = new User(1l, "uuid1","usertest1","user1test","Sirius Black", ROLE_1, false);
	 
	Book BOOK_1 = new Book(1L, "Lord of The Rings", "AVAILABLE", false);
	Book BOOK_2 = new Book(2L, "Lord of The Rings 2", "AVAILABLE", false);
	

	
	@Test
	public void getBookList() throws Exception{
		List<Book> books = new ArrayList<Book>();
		books.add(BOOK_1);
		books.add(BOOK_2);
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.findAll()).thenReturn(books);

				
		mockMvc.perform(MockMvcRequestBuilders
				.get("/book/all").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
	}
	
	@Test
	public void createBook() throws Exception{
		BookObject bookObj_1 = new BookObject("Doreamon No 32");
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.create(bookObj_1)).thenReturn(Optional.of(BOOK_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.post("/book/create")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(bookObj_1)))
				.andExpect(status().isOk())
				.andReturn();		
	}
	
	@Test
	public void updateBook() throws Exception{
		BookObject bookObj_1 = new BookObject("Doreamon No 32");
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.update(bookObj_1, 1L)).thenReturn(Optional.of(BOOK_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.put("/book/update/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(bookObj_1)))
				.andExpect(status().isOk())
				.andReturn();		
	}
	
	@Test
	public void getBook() throws Exception{
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.findByBookId(1L)).thenReturn(Optional.of(BOOK_1));
		mockMvc.perform(MockMvcRequestBuilders
				.put("/book/get/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
	@Test
	public void borrowBook() throws Exception{
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.borrow(1L)).thenReturn(Optional.of(BOOK_1));
		mockMvc.perform(MockMvcRequestBuilders
				.post("/book/borrow/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
	@Test
	public void returnBook() throws Exception{
		Mockito.when(authentication.findByToken("uuid1")).thenReturn(Optional.of(USER_1));
		Mockito.when(bookService.borrow(1L)).thenReturn(Optional.of(BOOK_1));
		mockMvc.perform(MockMvcRequestBuilders
				.post("/book/return/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
	
	@Test
	public void deleteBook() throws Exception{
		Mockito.when(bookService.delete(1L)).thenReturn(Optional.of(true));
		mockMvc.perform(MockMvcRequestBuilders
				.put("/book/delete/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();	
	}
}
