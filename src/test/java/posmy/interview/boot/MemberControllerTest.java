package posmy.interview.boot;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

@SpringBootTest
public class MemberControllerTest {
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
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member borrow book")
	void testBorrowBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(1L);
		
		mvc.perform(get("/member/books/borrow/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member borrow book - book already borrowed")
	void testBorrowBookByMember_book_already_borrowed() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/borrow/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member borrow book - missing fields")
	void testBorrowBookByMember_missing_fields() throws Exception {
		BookDto bookDto = new BookDto();
		
		mvc.perform(get("/member/books/borrow/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member borrow book - no records")
	void testBorrowBookByMember_no_record_found() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(10L);
		
		mvc.perform(get("/member/books/borrow/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="Kenny", roles= {"MEMBER"})
	@DisplayName("Member return book")
	void testReturnBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/return/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member return book - invalid returnee")
	void testReturnBookByMember_invalid_returnee() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/return/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="Kenny", roles= {"MEMBER"})
	@DisplayName("Member return book - no records found")
	void testReturnBookByMember_no_records_found() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(10L);
		
		mvc.perform(get("/member/books/return/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="Kenny", roles= {"MEMBER"})
	@DisplayName("Member return book - missing fields")
	void testReturnBookByMember_missing_fields() throws Exception {
		BookDto bookDto = new BookDto();
		
		mvc.perform(get("/member/books/return/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member get book")
	void testGetBookByMember() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/get/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member get book - no records found")
	void testGetBookByMember_no_record_found() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(5L);
		
		mvc.perform(get("/member/books/get/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member get account info")
	void testGetAccountByMember() throws Exception {
		mvc.perform(get("/member/accounts/info")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="Cathy", roles= {"MEMBER"})
	@DisplayName("Member get account info - no records found")
	void testGetAccountByMember_no_record_found() throws Exception {
		
		mvc.perform(get("/member/accounts/info")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="Ben", roles= {"MEMBER"})
	@DisplayName("Member deactivate account")
	void testDeleteAccountByMember() throws Exception {
		mvc.perform(get("/member/accounts/deactivate")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser(value="Cathy", roles= {"MEMBER"})
	@DisplayName("Member deactivate account - no records found")
	void testDeleteAccountByMemberr_no_record_found() throws Exception {
		mvc.perform(get("/member/accounts/deactivate")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian borrow book")
	void testBorrowBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(1L);
		
		mvc.perform(get("/member/books/borrow/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian return book")
	void testReturnBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/return/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian get book")
	void testGetBookByLibrarian() throws Exception {
		BookDto bookDto = new BookDto();
		bookDto.setId(3L);
		
		mvc.perform(get("/member/books/get/" + bookDto.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(bookDto)))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian get account info")
	void testGetAccountByLibrarian() throws Exception {
		
		mvc.perform(get("/member/accounts/info")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(value="John", roles= {"LIBRARIAN"})
	@DisplayName("Librarian deactivate account")
	void testDeleteAccountByLibrarian() throws Exception {
		mvc.perform(get("/member/accounts/deactivate")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isForbidden());
	}
}
