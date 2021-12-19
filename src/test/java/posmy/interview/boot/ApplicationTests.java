package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		mvc = webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@DisplayName("Users must be authorized in order to perform actions")
	void contextLoads() throws Exception {
		mvc.perform(get("/")).andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Check user status")
	public void user() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(get("/system/check/user/status").contentType("application/json;charset=UTF-8")
				.param("username", "true").content(objectMapper.writeValueAsString(user.getUsername())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian view member")
	public void member() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(get("/system/view/member").contentType("application/json;charset=UTF-8")
				.param("username", "true").content(objectMapper.writeValueAsString(user.getUsername())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian add new book")
	public void addBook() throws Exception {
		Book book = new Book(1, "Lifecycle", "Biology", "Available", null);

		mvc.perform(post("/system/add/{username}", "Jackson").contentType("application/json;charset=UTF-8")
				.param("reqBook", "true").content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian update latest book details")
	public void updateBook() throws Exception {
		Book book = new Book(1, "Lifecycle", "Biology", "Available", null);

		mvc.perform(put("/system/update/{username}", "Jackson").contentType("application/json;charset=UTF-8")
				.param("reqBook", "true").content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian remove book")
	public void removeBook() throws Exception {
		Book book = new Book(1, "Lifecycle", "Biology", "Available", null);

		mvc.perform(put("/system/remove/{username}", "Jackson").contentType("application/json;charset=UTF-8")
				.param("reqBook", "true").content(objectMapper.writeValueAsString(book)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Librarian add new member")
	public void addMember() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(post("/system/add/member").contentType("application/json;charset=UTF-8")
				.param("reqUser", "true").content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian update existing user details")
	public void updateMember() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(put("/system/update/member").contentType("application/json;charset=UTF-8")
				.param("reqUser", "true").content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Librarian remove member")
	public void removeMember() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(put("/system/remove/member").contentType("application/json;charset=UTF-8")
				.param("reqUser", "true").content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Member view book")
	public void book() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(get("/system/view/book").contentType("application/json;charset=UTF-8")
				.param("username", "true").content(objectMapper.writeValueAsString(user.getUsername())))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Member borrow the book")
	public void borrowBook() throws Exception {
		Book book = new Book(1, "Lifecycle", "Biology", "Available", null);
		Book book1 = new Book(2, "Nice To Meet You", "Funny", "Available", null);
		List<Book> b = new ArrayList<Book>();
		b.add(book);
		b.add(book1);

		mvc.perform(put("/system/borrow/book/{username}", "Jackson").contentType("application/json;charset=UTF-8")
				.param("bookReq", "true").content(objectMapper.writeValueAsString(b.get(0))))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Member return book")
	public void returnBook() throws Exception {
		Book book = new Book(1, "Lifecycle", "Biology", "Available", null);
		Book book1 = new Book(2, "Nice To Meet You", "Funny", "Available", null);
		List<Book> b = new ArrayList<Book>();
		b.add(book);
		b.add(book1);

		mvc.perform(put("/system/return/book/{username}", "Jackson").contentType("application/json;charset=UTF-8")
				.param("bookReq", "true").content(objectMapper.writeValueAsString(b.get(0))))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Member delete own account")
	public void deleteOwn() throws Exception {
		User user = new User(1, "Jackson", Boolean.FALSE, 2);

		mvc.perform(put("/system/delete/account").contentType("application/json;charset=UTF-8")
				.param("user", "true").content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());
	}
}
