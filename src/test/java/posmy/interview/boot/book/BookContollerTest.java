package posmy.interview.boot.book;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookController.class)
class BookContollerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookService bookService;
	
	@Autowired
	private ApplicationContext context;
	
//	@BeforeEach
//	void init() { 
//		h2Debug();
//	}
	
	@Test
	@WithMockUser(username = "librarian", password = "librarian")
	void givenLibrarian_whenGetAllBooks_thenSuccess() throws Exception {
		
		mockMvc.perform(get("/api/book")
				.queryParam("offset", "0")
				.queryParam("limit", "10"))
			.andDo(print())
			.andExpect(status().isOk());
//			.andExpect(content().string(containsString("")));
	}
	
	@Test
	@WithMockUser(username = "member", password = "member")
	void givenMember_whenGetAllBooks_thenSuccess() throws Exception {
		
		mockMvc.perform(get("/api/book")
				.queryParam("offset", "0")
				.queryParam("limit", "10"))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	
	
//	private void h2Debug() {
//		try {
//			Server.startWebServer(((DataSource)context.getBean("h2DataSource")).getConnection());
//		} catch (BeansException | SQLException e) {
//			e.printStackTrace();
//		}
//	}

	
	
}
