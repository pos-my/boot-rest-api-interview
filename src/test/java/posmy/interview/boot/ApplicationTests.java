package posmy.interview.boot;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import posmy.interview.boot.model.requestModel.BookRegisterRequest;
import posmy.interview.boot.model.requestModel.BookUpdateRequest;
import posmy.interview.boot.model.requestModel.FirstTimeRegisterRequest;
import posmy.interview.boot.model.requestModel.LoginRequest;
import posmy.interview.boot.model.requestModel.RegisterRequest;
import posmy.interview.boot.model.requestModel.UpdateUserRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class ApplicationTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Autowired
	ObjectMapper mapper;


	@BeforeEach
	void setup() {
		mvc = webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@DisplayName("Users must be authorized in order to perform actions")
	void contextLoads() throws Exception {
		mvc.perform(get("/"))
		.andExpect(status().isUnauthorized());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String accessToken(String username, String password) throws Exception{

		LoginRequest req = new LoginRequest();
		req.setUsername(username);
		req.setPassword(password);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(getRootUrl()+"/users/signin").content(asJsonString(req)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		String responseAsString = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");

		return "Bearer "+responseAsString;
	}

	@Order(1)
	@Test
	public void firstTimelogin() throws Exception {

		FirstTimeRegisterRequest req = new FirstTimeRegisterRequest();
		req.setUsername("royce");
		req.setPassword("admin");

		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/users/firstSignup")
				.content(asJsonString(req))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(2)
	@Test
	public void signin() throws Exception {

		LoginRequest req = new LoginRequest();
		req.setUsername("royce");
		req.setPassword("admin");

		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/users/signin")
				.content(asJsonString(req))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}
	@Order(3)
	@Test
	public void signupWithLibrarianAuth() throws Exception {

		String token = this.accessToken("royce", "admin");

		RegisterRequest req = new RegisterRequest();
		req.setUsername("royce2");
		req.setPassword("abc123");
		Set<String> roles = new HashSet<>();
		roles.add("member");
		req.setRole(roles);
		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/users/signup").header("Authorization", token)
				.content(asJsonString(req))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

		req = new RegisterRequest();
		req.setUsername("royce3");
		req.setPassword("123456");
		roles = new HashSet<>();
		roles.add("member");
		req.setRole(roles);
		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/users/signup").header("Authorization", token)
				.content(asJsonString(req))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(4)
	@Test
	public void signupWithMemberAuth() throws Exception {

		String token = this.accessToken("royce2", "abc123");

		RegisterRequest req = new RegisterRequest();
		req.setUsername("royce3");
		req.setPassword("123456");
		Set<String> roles = new HashSet<>();
		roles.add("member");
		req.setRole(roles);
		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/users/signup").header("Authorization", token)
				.content(asJsonString(req))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());



	}

	@Order(5)
	@Test
	public void getUserList() throws Exception {

		String token = this.accessToken("royce", "admin");

		mvc.perform(MockMvcRequestBuilders
				.get(getRootUrl()+"/users/getAllUsers").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(6)
	@Test
	public void getUserListWithMemAuth() throws Exception {

		String token = this.accessToken("royce2", "abc123");

		mvc.perform(MockMvcRequestBuilders
				.get(getRootUrl()+"/users/getAllUsers").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());

	}

	@Order(7)
	@Test
	public void getUserListById() throws Exception {

		String token = this.accessToken("royce", "admin");

		mvc.perform(MockMvcRequestBuilders
				.get(getRootUrl()+"/users/getUsers/1").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(8)
	@Test
	public void updateUser() throws Exception {

		String token = this.accessToken("royce", "admin");

		UpdateUserRequest update = new UpdateUserRequest();
		update.setUsername("ravehaseo");

		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/users/update/3").header("Authorization", token)
				.content(asJsonString(update))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(9)
	@Test
	public void deleteUser() throws Exception {

		String token = this.accessToken("royce", "admin");



		mvc.perform(MockMvcRequestBuilders
				.delete(getRootUrl()+"/users/delete/3").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	//Member Book

	@Order(10)
	@Test
	public void getMemberBook() throws Exception {

		String token = this.accessToken("royce2", "abc123");

		mvc.perform(MockMvcRequestBuilders
				.get(getRootUrl()+"/mem/books").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(11)
	@Test
	public void borrowBook() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/borrowBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(12)
	@Test
	public void borrowSameBook() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/borrowBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

	}



	@Order(13)
	@Test
	public void returnBook() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/returnBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(14)
	@Test
	public void returnDifferentBook() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/returnBook/3").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

	}

	//Librarian Book

	@Order(15)
	@Test
	public void getLibrarianBook() throws Exception {

		String token = this.accessToken("royce", "admin");

		mvc.perform(MockMvcRequestBuilders
				.get(getRootUrl()+"/lib/books").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}

	@Order(16)
	@Test
	public void addBook() throws Exception {

		String token = this.accessToken("royce", "admin");

		BookRegisterRequest book = new BookRegisterRequest();
		book.setBookCode("A01");
		book.setBookName("Legend of Dragoon");

		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/lib/addBook").header("Authorization", token)
				.content(asJsonString(book))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());



	}

	@Order(17)
	@Test
	public void updateBook() throws Exception {

		String token = this.accessToken("royce", "admin");

		BookUpdateRequest book = new BookUpdateRequest();
		book.setBookCode("A02");

		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/lib/updateBook/4").header("Authorization", token)
				.content(asJsonString(book))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());	
	}

	@Order(18)
	@Test
	public void removeBook() throws Exception {

		String token = this.accessToken("royce", "admin");


		mvc.perform(MockMvcRequestBuilders
				.delete(getRootUrl()+"/lib/removeBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());	
	}

	@Order(19)
	@Test
	public void addBookMemAuth() throws Exception {

		String token = this.accessToken("royce2", "abc123");

		BookRegisterRequest book = new BookRegisterRequest();
		book.setBookCode("A01");
		book.setBookName("Legend of Dragoon");

		mvc.perform(MockMvcRequestBuilders
				.post(getRootUrl()+"/lib/addBook").header("Authorization", token)
				.content(asJsonString(book))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());



	}

	@Order(20)
	@Test
	public void removeBookMemAuth() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.delete(getRootUrl()+"/lib/removeBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());



	}    

	@Order(21)
	@Test
	public void borrowBookLibAuth() throws Exception {

		String token = this.accessToken("royce", "admin");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/borrowBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());

	}

	@Order(22)
	@Test
	public void returnBookLibAuth() throws Exception {

		String token = this.accessToken("royce", "admin");


		mvc.perform(MockMvcRequestBuilders
				.put(getRootUrl()+"/mem/returnBook/2").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());

	}

	@Order(23)
	@Test
	public void deleteAccount() throws Exception {

		String token = this.accessToken("royce2", "abc123");


		mvc.perform(MockMvcRequestBuilders
				.delete(getRootUrl()+"/mem/deleteAccount").header("Authorization", token)    			
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());	
	}




}
