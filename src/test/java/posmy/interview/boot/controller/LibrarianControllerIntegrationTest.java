package posmy.interview.boot.controller;

import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.model.request.BookAddRequest;
import posmy.interview.boot.model.request.BookPutRequest;
import posmy.interview.boot.repos.BookRepository;
import posmy.interview.boot.repos.MyUserRepository;
import posmy.interview.boot.util.Constants;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibrarianControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private HttpHeaders headers;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        resetUsersWithDefault();

        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user001:pass"));
    }
    private void resetUsersWithDefault() {
        Try.run(() -> myUserRepository.deleteAll());
        MyUser defaultLibrarian = MyUser.builder()
                .username(Constants.DEFAULT_LIBRARIAN_USERNAME)
                .password(passwordEncoder.encode(Constants.DEFAULT_LIBRARIAN_PASSWORD))
                .authority(MyRole.LIBRARIAN.authority)
                .build();
        MyUser defaultMember = MyUser.builder()
                .username(Constants.DEFAULT_MEMBER_USERNAME)
                .password(passwordEncoder.encode(Constants.DEFAULT_MEMBER_PASSWORD))
                .authority(MyRole.MEMBER.authority)
                .build();
        myUserRepository.save(defaultLibrarian);
        myUserRepository.save(defaultMember);
    }

    @Test
    @DisplayName("Users with role LIBRARIAN are authorized to perform LIBRARIAN actions")
    void getWithLibrarianAuthorizationThenReturnSuccess() {
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
    void getWithMemberAuthorizationThenForbidden() {
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
    void getWithoutAuthorizationThenUnauthorized() {
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
    void getWithInvalidAuthorizationThenUnauthorized() {
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
    void bookAddGivenNameThenSaveNewBook() {
        BookAddRequest bookAddRequest = BookAddRequest.builder()
                .name("book name")
                .desc(null)
                .build();
        Book expectedBook = Book.builder()
                .name(bookAddRequest.getName())
                .status(BookStatus.AVAILABLE)
                .borrowRecords(new ArrayList<>())
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
    void bookPutThenUpdateExistingBook() {
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
                .borrowRecords(new ArrayList<>())
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
    void bookPutGivenInvalidIdThenError10000() {
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
    void bookDeleteThenDeleteExistingBook() {
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
    void bookDeleteGivenNonExistingIdThenDoNothing() {
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
