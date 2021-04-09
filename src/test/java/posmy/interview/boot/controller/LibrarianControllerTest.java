package posmy.interview.boot.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static posmy.interview.boot.utils.CommonConstants.AUTHORIZATION_HEADER;
import static posmy.interview.boot.utils.CommonConstants.BEARER_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.authentication.JwtRequest;
import posmy.interview.boot.authentication.JwtResponse;
import posmy.interview.boot.dto.request.CreateBookDto;
import posmy.interview.boot.dto.request.CreateUserDto;
import posmy.interview.boot.dto.request.UpdateBookDto;
import posmy.interview.boot.dto.request.UpdateUserDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.service.BookService;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.testutils.factories.BookFactory;
import posmy.interview.boot.testutils.factories.UserFactory;

@SpringBootTest
class LibrarianControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    /**
     * Test for unauthorized scenario without valid / empty token
     *
     * @throws Exception
     */
    @Test
    @DisplayName("Unauthorized to add a new book without valid token")
    void shouldNotCreateBookWithoutValidToken() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookService.createBook(any())).thenReturn(mockBook);

        mockMvc
            .perform(
                post("/api/posmy/librarian/book")
                    .content(objectMapper.writeValueAsString(CreateBookDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.responseCode", is(HttpStatus.UNAUTHORIZED.value())));

        verify(bookService, times(0)).createBook(any(CreateBookDto.class));
    }

    /**
     * Test for forbidden scenario without librarian role
     *
     * @throws Exception
     */
    @Test
    @DisplayName("Forbidden to add a new book without librarian role")
    void shouldNotCreateBookWithoutLibrarianRole() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookService.createBook(any())).thenReturn(mockBook);

        mockMvc
            .perform(
                post("/api/posmy/librarian/book")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("member"))
                    .content(objectMapper.writeValueAsString(CreateBookDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isForbidden());

        verify(bookService, times(0)).createBook(any(CreateBookDto.class));
    }

    @Test
    @DisplayName("Should add a new book")
    void shouldCreateBook() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookService.createBook(any())).thenReturn(mockBook);

        mockMvc
            .perform(
                post("/api/posmy/librarian/book")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
                    .content(objectMapper.writeValueAsString(CreateBookDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)));

        verify(bookService, times(1)).createBook(any(CreateBookDto.class));
    }

    @Test
    @DisplayName("Should update existing book")
    void shouldUpdateBook() throws Exception {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookService.updateBook(anyLong(), any())).thenReturn(mockBook);

        mockMvc
            .perform(
                patch("/api/posmy/librarian/book/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
                    .content(objectMapper.writeValueAsString(UpdateBookDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));

        verify(bookService, times(1)).updateBook(anyLong(), any(UpdateBookDto.class));
    }

    @Test
    @DisplayName("Should delete a book")
    void shouldDeleteBook() throws Exception {
        mockMvc
            .perform(
                delete("/api/posmy/librarian/book/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
            )
            .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(anyLong());
    }

    @Test
    @DisplayName("Should add a new user")
    void shouldCreateUserWithRoleMember() throws Exception {
        User mockUser = UserFactory.getInstance().constructUser();

        when(userService.createUser(any())).thenReturn(mockUser);

        mockMvc
            .perform(
                post("/api/posmy/librarian/user")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
                    .content(objectMapper.writeValueAsString(CreateUserDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", is(1)));

        verify(userService, times(1)).createUser(any(CreateUserDto.class));
    }

    @Test
    @DisplayName("Should update existing user")
    void shouldUpdateUser() throws Exception {
        User mockUser = UserFactory.getInstance().constructUser();

        when(userService.updateUser(anyLong(), any())).thenReturn(mockUser);

        mockMvc
            .perform(
                patch("/api/posmy/librarian/user/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
                    .content(objectMapper.writeValueAsString(UpdateUserDto.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)));

        verify(userService, times(1)).updateUser(anyLong(), any(UpdateUserDto.class));
    }

    @Test
    @DisplayName("Should delete a user")
    void shouldDeleteUser() throws Exception {
        mockMvc
            .perform(
                delete("/api/posmy/librarian/user/1")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken("librarian"))
            )
            .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserBy(anyLong());
    }

    private String obtainJwtToken(String username) throws Exception {
        ResultActions result = mockMvc.perform(
            post("/api/posmy/token")
                .content(objectMapper.writeValueAsString(
                    JwtRequest.builder().username(username).password("password").expiration(1000)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JwtResponse response = objectMapper.readValue(resultString, JwtResponse.class);

        return BEARER_TOKEN + response.getToken();
    }
}
