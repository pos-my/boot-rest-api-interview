package posmy.interview.boot.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static posmy.interview.boot.utils.CommonConstants.AUTHORIZATION_HEADER;
import static posmy.interview.boot.utils.CommonConstants.BEARER_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.authentication.JwtRequest;
import posmy.interview.boot.authentication.JwtResponse;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.testutils.factories.BookFactory;

@SpringBootTest
class MemberControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Should get books")
    void shouldGetBooks() throws Exception {
        int pageSize = 10;
        int pageNumber = 0;
        List<Book> mockBooks = BookFactory.getInstance().constructListOfBook(BookStatus.AVAILABLE);
        Page<Book> mockBookPage = new PageImpl<>(mockBooks);

        when(bookService.getBooks(any(), any())).thenReturn(mockBookPage);

        mockMvc
            .perform(
                get("/api/posmy/member/books")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("member"))
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(1)))
            .andExpect(
                jsonPath(
                    "$.content[0].title",
                    is("title")
                )
            );

        verify(bookService, times(1)).getBooks(any(), any());
    }

    @Test
    @DisplayName("Should borrow a book")
    void shouldBorrowBook() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.BORROWED);

        when(bookService.borrowBook(anyLong(), anyString())).thenReturn(mockBook);

        mockMvc
            .perform(
                post("/api/posmy/member/book/borrow/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("member"))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("BORROWED")));

        verify(bookService, times(1)).borrowBook(anyLong(), anyString());
    }

    @Test
    @DisplayName("Should return a book")
    void shouldReturnBook() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookService.returnBook(anyLong())).thenReturn(mockBook);

        mockMvc
            .perform(
                post("/api/posmy/member/book/return/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("member"))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("AVAILABLE")));

        verify(bookService, times(1)).returnBook(anyLong());
    }

    @Test
    @DisplayName("Should delete own account")
    void shouldDeleteOwnAccount() throws Exception {
        mockMvc
            .perform(
                delete("/api/posmy/member/user")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("member"))
            )
            .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserBy(anyString());
    }

    private String obtainJwtToken(String username) throws Exception {
        ResultActions result = mockMvc.perform(
            post("/api/posmy/token")
                .content(objectMapper.writeValueAsString(
                    JwtRequest.builder().username(username).password("password").expiration(1000)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JwtResponse response = objectMapper.readValue(resultString, JwtResponse.class);

        return BEARER_TOKEN + response.getToken();
    }
}
