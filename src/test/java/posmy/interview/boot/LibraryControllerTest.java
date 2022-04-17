package posmy.interview.boot;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.MemberDto;

@SpringBootTest
public class LibraryControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Autowired
    ObjectMapper mapper;
	
	@BeforeEach
	void setup() {
		mvc = webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian add book")
	void testAddBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("Phantom of the Opera");
		bookDto.setDescription("English literature");
		bookDto.setRemarks("Only have hardcopy");
		
		mvc.perform(post("/library/books/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian add book - missing fields")
	void testAddBookByLibrarian_missing_fields() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("Phantom of the Opera");
		bookDto.setRemarks("Only have hardcopy");
		
		mvc.perform(post("/library/books/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update book")
	void testUpdateBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);
		bookDto.setTitle("Tarzan");
		bookDto.setDescription("English literature - published 2020");
		
		mvc.perform(put("/library/books/update/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update book - missing fields")
	void testUpdateBookByLibrarian_missing_fields() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);
		bookDto.setTitle("Tarzan");
		
		mvc.perform(put("/library/books/update/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update book - non-identical ids")
	void testUpdateBookByLibrarian_non_identical_ids() throws Exception {
		Long id = 3L;
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);
		bookDto.setTitle("Tarzan");
		
		mvc.perform(put("/library/books/update/" + id)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update book - no records")
	void testUpdateBookByLibrarian_no_record_found() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(10L);
		bookDto.setTitle("Tarzan");
		bookDto.setDescription("English literature - published 2020");
		bookDto.setStatus("A");
		
		mvc.perform(put("/library/books/update/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete book")
	void testDeleteBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);

		mvc.perform(delete("/library/books/delete/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete book - no records found")
	void testDeleteBookByLibrarian_no_record_found() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(10L);

		mvc.perform(delete("/library/books/delete/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete book - missing fields")
	void testDeleteBookByLibrarian_missing_fields() throws Exception {
		BookDto bookDto = new BookDto();

		mvc.perform(delete("/library/books/delete/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian add member")
	void testAddMemberByLibrarian() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setFullName("Lily");
		
		mvc.perform(post("/library/members/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian add member - missing fields")
	void testAddMemberByLibrarian_missing_fields() throws Exception {
		MemberDto memberDto = new MemberDto();
		
		mvc.perform(post("/library/members/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update member")
	void testUpdateMemberByLibrarian() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		memberDto.setFullName("Lily");
		
		mvc.perform(put("/library/members/update/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update member - no records found")
	void testUpdateMemberByLibrarian_no_record_found() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(10L);
		memberDto.setFullName("Lily");
		
		mvc.perform(put("/library/members/update/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update member - missing fields")
	void testUpdateMemberByLibrarian_missing_fields() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(put("/library/members/update/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian update member - non-identical ids")
	void testUpdateMemberByLibrarian_non_identical_ids() throws Exception {
		Long id = 2L;
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(put("/library/members/update/" + id)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete member")
	void testDeleteMemberByLibrarian() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(delete("/library/members/delete/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete member - no records found")
	void testDeleteMemberByLibrarian_no_record_found() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(10L);
		
		mvc.perform(delete("/library/members/delete/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian delete member - missing fields")
	void testDeleteMemberByLibrarian_missing_fields() throws Exception {
		MemberDto memberDto = new MemberDto();
		
		mvc.perform(delete("/library/members/delete/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian get member")
	void testGetMemberByLibrarian() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(get("/library/members/get/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian get member - no records found")
	void testGetMemberByLibrarian_no_record_found() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(10L);
		
		mvc.perform(get("/library/members/get/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian get member - missing fields")
	void testGetMemberByLibrarian_missing_fields() throws Exception {
		MemberDto memberDto = new MemberDto();
		
		mvc.perform(get("/library/members/get/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member add book")
	void testAddBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setTitle("Phantom of the Opera");
		bookDto.setDescription("English literature");
		bookDto.setRemarks("Only have hardcopy");
		
		mvc.perform(post("/library/books/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member update book")
	void testUpdateBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);
		bookDto.setTitle("Tarzan");
		bookDto.setDescription("English literature - published 2020");
		
		mvc.perform(put("/library/books/update/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member delete book")
	void testDeleteBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(2L);

		mvc.perform(delete("/library/books/delete/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member add member")
	void testAddMemberByMember() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setFullName("Lily");
		
		mvc.perform(post("/library/members/add")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member update member")
	void testUpdateMemberByMember() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		memberDto.setFullName("Lily");
		
		mvc.perform(put("/library/members/update/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(memberDto)))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member delete member")
	void testDeleteMemberByMember() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(delete("/library/members/delete/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member get member")
	void testGetMemberByMember() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(3L);
		
		mvc.perform(get("/library/members/get/" + memberDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
}
