package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

    private static final String MEMBER_API = "/api/v1/member";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Action: Views all available books
    // Actor: Member
    // Result: Able to view all available books
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberViewsAvailableBooks() throws Exception {
        mockMvc.perform(get(MEMBER_API + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is("b1")))
                .andExpect(jsonPath("$[0].name", is("Book 1")))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[1].id", is("b2")))
                .andExpect(jsonPath("$[1].name", is("Book 2")))
                .andExpect(jsonPath("$[1].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[2].id", is("b3")))
                .andExpect(jsonPath("$[2].name", is("Book 3")))
                .andExpect(jsonPath("$[2].status", is("AVAILABLE")));
    }

    // Action: Member borrows available book
    // Actor: Member
    // Result: Book is borrowed
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberBorrowsAvailableBook() throws Exception {
        mockMvc.perform(post(MEMBER_API + "/books/b1?action=borrow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.status", is("BORROWED")))
                .andExpect(jsonPath("$.user.loginId", is("member1")));
    }

    // Action: Member borrows borrowed book
    // Actor: Member
    // Result: Gets 500 internal server error
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberBorrowsBorrowedBook() throws Exception {
        mockMvc.perform(post(MEMBER_API + "/books/b4?action=borrow"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Book with ID b4 is already borrowed!"));
    }

    // Action: Member returns borrowed book
    // Actor: Member
    // Result: Book is returned
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberReturnsBorrowedBook() throws Exception {
        mockMvc.perform(post(MEMBER_API + "/books/b4?action=return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.status", is("AVAILABLE")))
                .andExpect(jsonPath("$.user", is(nullValue())));
    }

    // Action: Member returns available book
    // Actor: Member
    // Result: Gets 500 internal server error
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberReturnsAvailableBook() throws Exception {
        mockMvc.perform(post(MEMBER_API + "/books/b1?action=return"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Book with ID b1 is already returned!"));
    }

    // Action: Member tries to throw book away
    // Actor: Member
    // Result: Gets 500 internal server error
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberTriesToThrowBookAway() throws Exception {
        mockMvc.perform(post(MEMBER_API + "/books/b1?action=throwaway"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("throwaway is not a valid action!"));
    }
}