package posmy.interview.boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.repos.BookRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibrarianControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    private HttpHeaders headers;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user001:pass"));
    }

    @Test
    @DisplayName("Users with role LIBRARIAN are authorized to perform LIBRARIAN actions")
    public void getWithLibrarianAuthorizationThenReturnSuccess() {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getBody()).isEqualTo("success");
    }

    @Test
    @DisplayName("Users with role MEMBER are not authorized to perform LIBRARIAN actions")
    public void getWithMemberAuthorizationThenForbidden() {
        headers.replace(HttpHeaders.AUTHORIZATION,
                List.of(authorizationToken("user002:pass")));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Users without authorization token are not authorized to perform LIBRARIAN actions")
    public void getWithoutAuthorizationThenUnauthorized() {
        headers.remove(HttpHeaders.AUTHORIZATION);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Users with invalid authorization token are not authorized to perform LIBRARIAN actions")
    public void getWithInvalidAuthorizationThenUnauthorized() {
        headers.replace(HttpHeaders.AUTHORIZATION,
                List.of("anyInvalid"));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN add book")
    public void bookAddGivenNameThenSaveNewBook() {
        BookAddRequest bookAddRequest = BookAddRequest.builder()
                .name("book name")
                .desc(null)
                .build();
        Book expectedBook = Book.builder()
                .name(bookAddRequest.getName())
                .status(BookStatus.AVAILABLE)
                .build();

        HttpEntity<BookAddRequest> httpEntity = new HttpEntity<>(bookAddRequest, headers);
        ResponseEntity<Book> responseEntity = restTemplate.exchange(
                absoluteUrl("/book"),
                HttpMethod.POST,
                httpEntity,
                Book.class);
        assertThat(responseEntity.getBody())
                .isNotNull();
        assertThat(bookRepository.findById(responseEntity.getBody().getId()).orElseThrow())
                .usingRecursiveComparison().ignoringFields("id", "lastUpdateDt")
                .isEqualTo(expectedBook);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN update book")
    public void bookPutThenUpdateExistingBook() {
        Book existingBook = setupExistingBook();
        BookPutRequest bookPutRequest = BookPutRequest.builder()
                .id(existingBook.getId())
                .name("Book 001 NEW")
                .desc("Book of 001 NEW")
                .imageUrl("http://image.NEW")
                .status(BookStatus.BORROWED)
                .build();
        Book expectedBook = existingBook.toBuilder()
                .name(bookPutRequest.getName())
                .desc(bookPutRequest.getDesc())
                .imageUrl(bookPutRequest.getImageUrl())
                .status(bookPutRequest.getStatus())
                .build();

        HttpEntity<BookPutRequest> httpEntity = new HttpEntity<>(bookPutRequest, headers);
        ResponseEntity<Book> responseEntity = restTemplate.exchange(
                absoluteUrl("/book"),
                HttpMethod.PUT,
                httpEntity,
                Book.class);
        assertThat(responseEntity.getBody())
                .isNotNull();
        Book modifiedBook = bookRepository.findById(responseEntity.getBody().getId()).orElseThrow();
        assertThat(modifiedBook)
                .usingRecursiveComparison().ignoringFields("lastUpdateDt")
                .isEqualTo(expectedBook);
        assertThat(modifiedBook.getLastUpdateDt())
                .isAfter(existingBook.getLastUpdateDt());
    }

    @Test
    @DisplayName("Users with role LIBRARIAN update non-existing book will get error 10000")
    public void bookPutGivenInvalidIdThenError10000() {
        BookPutRequest bookPutRequest = BookPutRequest.builder()
                .id(UUID.randomUUID().toString())
                .name("Book 001 NEW")
                .desc("Book of 001 NEW")
                .imageUrl("http://image.NEW")
                .status(BookStatus.BORROWED)
                .build();

        HttpEntity<BookPutRequest> httpEntity = new HttpEntity<>(bookPutRequest, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                        absoluteUrl("/book"),
                        HttpMethod.PUT,
                        httpEntity,
                        Void.class);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(bookRepository.findById(bookPutRequest.getId()))
                .isEmpty();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN delete book")
    public void bookDeleteThenDeleteExistingBook() {
        Book existingBook = setupExistingBook();
        String deleteId = existingBook.getId();

        assertThat(bookRepository.findById(deleteId))
                .isNotEmpty();

        HttpEntity<BookPutRequest> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book/" + deleteId),
                HttpMethod.DELETE,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(deleteId))
                .isEmpty();
    }

    @Test
    @DisplayName("Users with role LIBRARIAN delete non-existing book")
    public void bookDeleteGivenNonExistingIdThenDoNothing() {
        String deleteId = UUID.randomUUID().toString();

        assertThat(bookRepository.findById(deleteId))
                .isEmpty();

        HttpEntity<BookPutRequest> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book/" + deleteId),
                HttpMethod.DELETE,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(bookRepository.findById(deleteId))
                .isEmpty();
    }

    private Book setupExistingBook() {
        Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("Book 001")
                .desc("Book of 001")
                .imageUrl("http://image")
                .status(BookStatus.AVAILABLE)
                .build();
        return bookRepository.save(book);
    }

    private String authorizationToken(String userPass) {
        String base64EncodedToken = Base64.getEncoder().encodeToString(
                userPass.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64EncodedToken;
    }

    private String absoluteUrl(String path) {
        return "http://localhost:" + port + "/v1/librarian" + path;
    }
}
