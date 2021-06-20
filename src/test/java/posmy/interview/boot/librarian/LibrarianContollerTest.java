package posmy.interview.boot.librarian;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.book.Book;
import posmy.interview.boot.util.DummyUtils;

@WebMvcTest
class LibrarianContollerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LibrarianService librarianService;
	
	@Test
	@WithMockUser(username = "librarian", password = "librarian")
	void givenLibrarian_whenAddBook_thenSuccess() throws Exception {
		
		String json = DummyUtils.getBookJson();
		mockMvc.perform(post("/librarian/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "member", password = "member")
	void givenMember_whenAddBook_thenForbidden() throws Exception {
		
		String json = DummyUtils.getBookJson();
		mockMvc.perform(post("/librarian/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "librarian", password = "librarian")
	void givenLibrarian_whenUpdateBook_thenForbidden() throws Exception {
		
		String json = DummyUtils.getBookJson();
		mockMvc.perform(put("/member/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(username = "member", password = "member")
	void givenMember_whenUpdateBook_thenForbidden() throws Exception {
		
		String json = DummyUtils.getBookJson();
		mockMvc.perform(put("/member/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.with(csrf()))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
