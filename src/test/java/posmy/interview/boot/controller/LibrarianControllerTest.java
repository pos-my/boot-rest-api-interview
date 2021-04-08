package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.UserDto;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@Transactional
public class LibrarianControllerTest {

    private static final String LIBRARIAN_API = "/api/v1/librarian";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Action: Views all books
    // Actor: Librarian
    // Result: All books returned
    @WithMockUser(username = "librarian100", roles = "LIBRARIAN")
    @Test
    public void librarianViewsAllBooks() throws Exception {
        mockMvc.perform(get(LIBRARIAN_API + "/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is("b1")))
                .andExpect(jsonPath("$[0].name", is("Book 1")))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[1].id", is("b2")))
                .andExpect(jsonPath("$[1].name", is("Book 2")))
                .andExpect(jsonPath("$[1].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[2].id", is("b3")))
                .andExpect(jsonPath("$[2].name", is("Book 3")))
                .andExpect(jsonPath("$[2].status", is("AVAILABLE")))
                .andExpect(jsonPath("$[3].id", is("b4")))
                .andExpect(jsonPath("$[3].name", is("Book 4")))
                .andExpect(jsonPath("$[3].status", is("BORROWED")))
                .andExpect(jsonPath("$[4].id", is("b5")))
                .andExpect(jsonPath("$[4].name", is("Book 5")))
                .andExpect(jsonPath("$[4].status", is("BORROWED")));
    }

    // Action: Views all books
    // Actor: Member
    // Result: Unable to view the books, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberViewsAllBooks() throws Exception {
        mockMvc.perform(get(LIBRARIAN_API + "/books"))
                .andExpect(status().isForbidden());
    }

    // Action: Create new book
    // Actor: Librarian
    // Result: Created book returned
    @WithMockUser(username = "librarian100", roles = "LIBRARIAN")
    @Test
    public void librarianCreatesBook() throws Exception {
        BookDto bookDto = BookDto.builder().id("book1").name("BOOK #1").build();
        mockMvc.perform(post(LIBRARIAN_API + "/books")
                .content(objectMapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.id").exists());
    }

    // Action: Create new book
    // Actor: Member
    // Result: Unable to create the book, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberCreatesBook() throws Exception {
        BookDto bookDto = BookDto.builder().id("book1").name("BOOK #1").build();
        mockMvc.perform(post(LIBRARIAN_API + "/books")
                .content(objectMapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Action: Update existing book
    // Actor: Librarian
    // Result: Updated book returned
    @WithMockUser(username = "librarian1", roles = "LIBRARIAN")
    @Test
    public void librarianUpdatesBook() throws Exception {
        BookDto bookDto = BookDto.builder().name("UPDATED book name").build();
        mockMvc.perform(put(LIBRARIAN_API + "/books/b1")
                .content(objectMapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.id", is("b1")))
                .andExpect(jsonPath("$.name", is("UPDATED book name")));
    }

    // Action: Update existing book
    // Actor: Member
    // Result: Unable to update the book, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberUpdatesBook() throws Exception {
        BookDto bookDto = BookDto.builder().id("book1").name("BOOK #1").build();
        mockMvc.perform(put(LIBRARIAN_API + "/books/b1")
                .content(objectMapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Action: Delete existing book
    // Actor: Librarian
    // Result: Book deleted
    @WithMockUser(username = "librarian1", roles = "LIBRARIAN")
    @Test
    public void librarianDeletesBook() throws Exception {
        mockMvc.perform(delete(LIBRARIAN_API + "/books/b1"))
                .andExpect(status().isOk());
    }

    // Action: Delete existing book
    // Actor: Member
    // Result: Unable to delete the book, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberDeletesBook() throws Exception {
        mockMvc.perform(delete(LIBRARIAN_API + "/books/b1"))
                .andExpect(status().isForbidden());
    }

    // Action: Views all members
    // Actor: Librarian
    // Result: All members returned
    @WithMockUser(username = "librarian100", roles = "LIBRARIAN")
    @Test
    public void librarianViewsAllMembers() throws Exception {
        mockMvc.perform(get(LIBRARIAN_API + "/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("u4")))
                .andExpect(jsonPath("$[0].name", is("Member 1")))
                .andExpect(jsonPath("$[0].roles[0].name", is("MEMBER")))
                .andExpect(jsonPath("$[1].id", is("u5")))
                .andExpect(jsonPath("$[1].name", is("Member 2")))
                .andExpect(jsonPath("$[1].roles[0].name", is("MEMBER")));
    }

    // Action: View all members
    // Actor: Member
    // Result: Unable to view the members, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberViewsAllMembers() throws Exception {
        mockMvc.perform(get(LIBRARIAN_API + "/members"))
                .andExpect(status().isForbidden());
    }

    // Action: Create new member
    // Actor: Librarian
    // Result: Created member returned
    @WithMockUser(username = "librarian100", roles = "LIBRARIAN")
    @Test
    public void librarianCreatesMember() throws Exception {
        UserDto userDto = UserDto.builder().loginId("newmember99").name("New Member 99").pass("newmemberpass").build();
        mockMvc.perform(post(LIBRARIAN_API + "/members")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.loginId", is("newmember99")))
                .andExpect(jsonPath("$.name", is("New Member 99")));
    }

    // Action: Create new member
    // Actor: Member
    // Result: Unable to create the member, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberCreatesMember() throws Exception {
        UserDto userDto = UserDto.builder().loginId("newmember99").name("New member 99").pass("newmemberpass").build();
        mockMvc.perform(post(LIBRARIAN_API + "/members")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Action: Updates existing member
    // Actor: Librarian
    // Result: Updated member returned
    @WithMockUser(username = "librarian100", roles = "LIBRARIAN")
    @Test
    public void librarianUpdatesMember() throws Exception {
        UserDto userDto = UserDto.builder().loginId("member1").name("New Member 88").build();
        mockMvc.perform(put(LIBRARIAN_API + "/members/member1")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.name", is("New Member 88")));
    }

    // Action: Update existing member
    // Actor: Member
    // Result: Unable to update the member, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberUpdatesMember() throws Exception {
        UserDto userDto = UserDto.builder().loginId("member1").name("New Member 88").build();
        mockMvc.perform(put(LIBRARIAN_API + "/members/member1")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Action: Delete existing member who borrowed 2 books
    // Actor: Librarian
    // Result: Member deleted, books borrowed by member returned
    @WithMockUser(username = "librarian1", roles = "LIBRARIAN")
    @Test
    public void librarianDeletesMember() throws Exception {
        mockMvc.perform(delete(LIBRARIAN_API + "/members/member1"))
                .andExpect(status().isOk());

        // Both b4 and b5 were initially borrowed by member1
        // Deleting member1 should make the books available again
        mockMvc.perform(get(LIBRARIAN_API + "/books/b4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));

        mockMvc.perform(get(LIBRARIAN_API + "/books/b5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.status", is("AVAILABLE")));
    }

    // Action: Delete existing member
    // Actor: Member
    // Result: Unable to delete the member, get 403 Forbidden
    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberDeletesMember() throws Exception {
        mockMvc.perform(delete(LIBRARIAN_API + "/members/member1"))
                .andExpect(status().isForbidden());
    }

}