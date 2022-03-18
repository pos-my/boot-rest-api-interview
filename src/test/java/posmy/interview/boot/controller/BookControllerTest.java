package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.controller.request.AddBooksRequest;
import posmy.interview.boot.db.BookRepository;
import posmy.interview.boot.entity.UserIdAndRole;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.jwt.JwtUtils;
import posmy.interview.boot.security.service.UserDetailsImpl;
import posmy.interview.boot.security.service.UserDetailsServiceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@RequiredArgsConstructor
public class BookControllerTest {

    private final WebApplicationContext context;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void fetchAllBooks() throws Exception {
        mockMemberAccessToken();

        mockMvc.perform(get("/book/list").header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addBooks() throws Exception {
        mockLibrarianAccessToken();

        List<String> newBookTitles = new ArrayList<>();
        newBookTitles.add("Book 1");
        newBookTitles.add("Book 2");
        AddBooksRequest request = new AddBooksRequest();
        request.setTitleList(newBookTitles);

        String requestBody = mapper.writeValueAsString(request);
        mockMvc.perform(post("/book/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void addBooksDuplicate() throws Exception {
        mockLibrarianAccessToken();

        List<String> newBookTitles = new ArrayList<>();
        newBookTitles.add("Book 1");
        newBookTitles.add("Book 2");
        AddBooksRequest request = new AddBooksRequest();
        request.setTitleList(newBookTitles);

        List<Book> existingBook = new ArrayList<>();
        existingBook.add(Book.builder().id(1L).title("Book 1").status(BookStatus.AVAILABLE).build());
        existingBook.add(Book.builder().id(2L).title("Book 2").status(BookStatus.AVAILABLE).build());

        given(bookRepository.findByTitleIn(newBookTitles)).willReturn(existingBook);

        String requestBody = mapper.writeValueAsString(request);

        Assertions.assertThatThrownBy(
                () -> mockMvc.perform(post("/book/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer accesTokenAbc123"))
        ).hasCauseInstanceOf(Exception.class).hasMessageContaining("Book already exist");
    }

    @Test
    void updateBooks() throws Exception {
        mockLibrarianAccessToken();

        List<String> newBookTitles = new ArrayList<>();
        newBookTitles.add("Book 1");
        newBookTitles.add("Book 2");
        AddBooksRequest request = new AddBooksRequest();
        request.setTitleList(newBookTitles);

        String requestBody = mapper.writeValueAsString(request);
        mockMvc.perform(post("/book/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setStatus(BookStatus.BORROWED);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setStatus(BookStatus.BORROWED);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);

        requestBody = mapper.writeValueAsString(bookList);

        mockMvc.perform(put("/book/update").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBook() throws Exception {
        mockLibrarianAccessToken();

        List<String> newBookTitles = new ArrayList<>();
        newBookTitles.add("Book 1");
        AddBooksRequest request = new AddBooksRequest();
        request.setTitleList(newBookTitles);

        String requestBody = mapper.writeValueAsString(request);
        mockMvc.perform(post("/book/add").contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());

        String bookIdToBeRemoved = "1";
        mockMvc.perform(delete("/book/delete/" + bookIdToBeRemoved)
                .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void borrowBook() throws Exception {
        mockMemberAccessToken();

        Book book = Book.builder()
            .id(1L)
            .title("Book 1")
            .status(BookStatus.AVAILABLE).build();

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        String bookIdToBeBorrowed = "1";
        mockMvc.perform(put("/book/borrow/" + bookIdToBeBorrowed)
                .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void borrowBorrowedBook() throws Exception {
        addBooks();
        mockMemberAccessToken();

        Book book = Book.builder()
            .id(1L)
            .title("Book 1")
            .status(BookStatus.BORROWED).build();

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        String bookIdToBeBorrowed = "1";
        Assertions.assertThatThrownBy(
                () -> mockMvc.perform(put("/book/borrow/" + bookIdToBeBorrowed)
                        .header("Authorization", "Bearer accesTokenAbc123"))
        ).hasCauseInstanceOf(Exception.class).hasMessageContaining("The book has been borrowed");
    }

    @Test
    void returnBook() throws Exception {
        addBooks();
        mockMemberAccessToken();

        Book book = Book.builder()
                .id(1L)
                .title("Book 1")
                .status(BookStatus.BORROWED).build();

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        String bookIdToBeBorrowed = "1";
        mockMvc.perform(put("/book/return/" + bookIdToBeBorrowed)
                        .header("Authorization", "Bearer accesTokenAbc123"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void returnUnborrowedBook() throws Exception {
        addBooks();
        mockMemberAccessToken();

        Book book = Book.builder()
                .id(1L)
                .title("Book 1")
                .status(BookStatus.AVAILABLE).build();

        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        String bookIdToBeBorrowed = "1";
        Assertions.assertThatThrownBy(
                () -> mockMvc.perform(put("/book/return/" + bookIdToBeBorrowed)
                        .header("Authorization", "Bearer accesTokenAbc123"))
        ).hasCauseInstanceOf(Exception.class).hasMessageContaining("The book has not been borrowed");
    }

    private void mockMemberAccessToken() {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, UserRole.MEMBER);
        User user = User.builder()
                .id(1L).username("username").password("password")
                .role(UserRole.MEMBER).state(UserState.ACTIVE)
                .createdTime(Instant.now()).updatedTime(Instant.now())
                .build();
        UserDetails userDetails = UserDetailsImpl.buildUser(user);

        given(jwtUtils.validateJwtToken(Mockito.anyString())).willReturn(true);
        given(jwtUtils.getUserRoleFromJwtToken(Mockito.anyString())).willReturn(userIdAndRole);
        given(userDetailsService.loadUserById(userIdAndRole.getId())).willReturn(userDetails);
    }

    private void mockLibrarianAccessToken() {
        UserIdAndRole userIdAndRole = new UserIdAndRole(1L, UserRole.LIBRARIAN);
        User user = User.builder()
                .id(1L).username("username").password("password")
                .role(UserRole.LIBRARIAN).state(UserState.ACTIVE)
                .createdTime(Instant.now()).updatedTime(Instant.now())
                .build();
        UserDetails userDetails = UserDetailsImpl.buildUser(user);

        given(jwtUtils.validateJwtToken(Mockito.anyString())).willReturn(true);
        given(jwtUtils.getUserRoleFromJwtToken(Mockito.anyString())).willReturn(userIdAndRole);
        given(userDetailsService.loadUserById(userIdAndRole.getId())).willReturn(userDetails);
    }
}
