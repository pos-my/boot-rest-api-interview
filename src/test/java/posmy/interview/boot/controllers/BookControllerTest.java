package posmy.interview.boot.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.dtos.book.BookDto;
import posmy.interview.boot.models.dtos.book.BookStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookControllerTest {

    private BookController bookControllerTest;
    private String baseUrl;
    private String librarianEmail;
    private Book testBook;
    private BookDto testBookDto;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        librarianEmail = "admin@email.com";
        testBook = new Book();
        testBook.setBookId(1);
        testBook.setTitle("The Grass is Always Greener");
        testBook.setAuthor("Jeffrey Archer");
        testBook.setPublishedYear("2011");
        testBook.setStatus(BookStatus.AVAILABLE);
        testBookDto = new BookDto();
        testBookDto.setTitle("New Book");
        testBookDto.setAuthor("New Author");
        testBookDto.setPublishedYear("2021");
        testBookDto.setStatus(BookStatus.AVAILABLE);
    }

    @Test
    void getBook_success() throws URISyntaxException {
        var uri = new URI(baseUrl + "/books/" + testBook.getBookId());
        var headers = new HttpHeaders();
        headers.add("Email", librarianEmail);
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    void getBook_missingCredentials() throws URISyntaxException {
        var uri = new URI(baseUrl + "/books/" + testBook.getBookId());
        var headers = new HttpHeaders();
        headers.add("Email", "");
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getBook_bookDoesNotExist() throws URISyntaxException {
        var uri = new URI(baseUrl + "/book/0");
        var headers = new HttpHeaders();
        headers.add("Email", librarianEmail);
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllBooks_success() throws URISyntaxException {
        var uri = new URI(baseUrl + "/books");
        var headers = new HttpHeaders();
        headers.add("Email", librarianEmail);
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    void getAllBooks_missingCredentials() throws URISyntaxException {
        var uri = new URI(baseUrl + "/books");
        var headers = new HttpHeaders();
        headers.add("Email", "");
        var requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).headers(headers).build();

        var response = new TestRestTemplate().exchange(requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void addBooks_librarian_success() throws URISyntaxException {
    }

    @Test
    void addBooks_librarian_failure() throws URISyntaxException {

    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBooks() {
    }

    @Test
    void borrowBook() {
    }

    @Test
    void returnBook() {
    }
}