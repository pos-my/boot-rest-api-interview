package posmy.interview.boot.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.util.TokenUtil;

import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {
    @Value("${security.authentication.secret}")
    private String secret;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Gson gson;

    @Test
    void canGetBook() throws Exception {
        //given
        String member_access_token = TokenUtil.generateAccessToken(
                "zz",
                "/login",
                Collections.singletonList("MEMBER"),
                secret
        );

        //when
        //then
        Book expectedBook = new Book(null, "0-2243-3869-2", "Brave New World", "Lois", "AVAILABLE", null, "English", 2010);
        mvc.perform(get("/book/get?isbn=" + expectedBook.getIsbn())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is(expectedBook.getIsbn())))
                .andExpect(jsonPath("$.title", is(expectedBook.getTitle())))
                .andExpect(jsonPath("$.author", is(expectedBook.getAuthor())))
                .andExpect(jsonPath("$.status", is(expectedBook.getStatus())));
    }

    @Test
    void getBooks() throws Exception {
        //given
        String member_access_token = TokenUtil.generateAccessToken(
                "zz",
                "/login",
                Collections.singletonList("MEMBER"),
                secret
        );

        //when
        //then
        mvc.perform(get("/book/get-all-books")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*").isNotEmpty());
    }

    @Test
    void saveBook() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        Book bookToBeSave = new Book(null, "0-5523-3054-1", "The Electronic Swagman", "Lois", "AVAILABLE", null, "English", 2010);

        //when
        //then
        mvc.perform(post("/book/save")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(gson.toJson(bookToBeSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn", is(bookToBeSave.getIsbn())))
                .andExpect(jsonPath("$.title", is(bookToBeSave.getTitle())))
                .andExpect(jsonPath("$.author", is(bookToBeSave.getAuthor())))
                .andExpect(jsonPath("$.status", is(bookToBeSave.getStatus())));
    }

    @Test
    void removeBook() throws Exception {
        //given
        String librarian_access_token = TokenUtil.generateAccessToken(
                "sean",
                "/login",
                Collections.singletonList("LIBRARIAN"),
                secret
        );
        String isbn = "0-2243-3869-2";

        //when
        //then
        mvc.perform(delete("/book/remove?isbn="+isbn)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + librarian_access_token))
                .andExpect(status().isOk());
    }

    @Test
    void borrowBook() throws Exception {
        //given
        String member_access_token = TokenUtil.generateAccessToken(
                "zz",
                "/login",
                Collections.singletonList("MEMBER"),
                secret
        );
        Book bookToBeBorrow = new Book(null, "0-2098-4472-8", "The Power of Now", "Arthur", "AVAILABLE", null, "English", 2013);

        //when
        //then
        mvc.perform(post("/book/borrow?isbn="+bookToBeBorrow.getIsbn())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(OK.value())))
                .andExpect(jsonPath("$.message", containsString("Book borrowed successfully, title")))
                .andExpect(jsonPath("$.message", containsString(bookToBeBorrow.getTitle())));
    }

    @Test
    void returnBook() throws Exception {
        //given
        String member_access_token = TokenUtil.generateAccessToken(
                "zz",
                "/login",
                Collections.singletonList("MEMBER"),
                secret
        );
        Book bookToBeReturn = new Book(null, "0-7686-2345-1", "About Time", "Margot Robbie", "BORROWED", null, "English", 2010);

        //when
        //then
        mvc.perform(post("/book/return?isbn="+bookToBeReturn.getIsbn())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + member_access_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(OK.value())))
                .andExpect(jsonPath("$.message", containsString("Book returned successfully, title")))
                .andExpect(jsonPath("$.message", containsString(bookToBeReturn.getTitle())));
    }
}