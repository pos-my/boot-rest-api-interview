package posmy.interview.boot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.BorrowRecord;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.repos.BookRepository;
import posmy.interview.boot.repos.BorrowRecordRepository;
import posmy.interview.boot.repos.MyUserRepository;
import posmy.interview.boot.util.Constants;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        resetUsersWithDefault();

        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                authorizationToken("user002:pass"));
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
    public void getWithMemberAuthorizationThenReturnSuccess() {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getBody()).isEqualTo("success");
    }

    @Test
    public void getWithLibrarianAuthorizationThenReturnSuccess() {
        headers.replace(HttpHeaders.AUTHORIZATION,
                List.of(authorizationToken("user001:pass")));
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl(""),
                HttpMethod.GET,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
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
    public void getBookWithPagingThenReturnPageOfBook() throws JsonProcessingException {
        setupBooks(5);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book?page={page}&size={size}"),
                HttpMethod.GET,
                httpEntity,
                String.class,
                "0", "2");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());
        assertTrue(jsonNode.at("/page/content").isArray());
        assertEquals(0, jsonNode.at("/page/number").intValue());
        assertEquals(2, jsonNode.at("/page/size").intValue());
        assertEquals(3, jsonNode.at("/page/totalPages").intValue());
        bookRepository.deleteAll();
    }

    @Test
    public void getBookWithPagingExceedsTotalPagesThenReturnEmptyPage() throws JsonProcessingException {
        setupBooks(5);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book?page={page}&size={size}"),
                HttpMethod.GET,
                httpEntity,
                String.class,
                "4", "2");
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(responseEntity.getBody());
        JsonNode jsonNode = new ObjectMapper().readTree(responseEntity.getBody());
        assertTrue(jsonNode.at("/page/content").isArray());
        assertEquals(4, jsonNode.at("/page/number").intValue());
        assertEquals(2, jsonNode.at("/page/size").intValue());
        assertEquals(0, jsonNode.at("/page/numberOfElements").intValue());
        assertEquals(3, jsonNode.at("/page/totalPages").intValue());
        bookRepository.deleteAll();
    }

    @Test
    public void borrowBookWithIdThenSuccess() {
        Book existingBook = setupExistingBook();
        assertThat(borrowRecordRepository.findAll().size()).isZero();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book/borrow/" + existingBook.getId()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(borrowRecordRepository.findAll().size()).isNotZero();
        assertThat(bookRepository.findById(existingBook.getId()).orElseThrow().getStatus())
                .isEqualTo(BookStatus.BORROWED);
        assertThat(bookRepository.findById(existingBook.getId()).orElseThrow().getBorrowRecords())
                .isNotEmpty();
        bookRepository.deleteAll();
        borrowRecordRepository.deleteAll();
    }

    @Test
    public void borrowBookWithIdWithNonEmptyRecordsThenSuccess() {
        Book existingBook = setupExistingBookWithRecords();
        assertThat(borrowRecordRepository.findAll().size()).isEqualTo(1);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                absoluteUrl("/book/borrow/" + existingBook.getId()),
                HttpMethod.PATCH,
                httpEntity,
                String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(borrowRecordRepository.findAll().size()).isEqualTo(2);
        assertThat(bookRepository.findById(existingBook.getId()).orElseThrow().getBorrowRecords().size())
                .isEqualTo(2);
        bookRepository.deleteAll();
        borrowRecordRepository.deleteAll();
    }

    private void setupBooks(int size) {
        for (int i = 0; i < size; i++) {
            Book book = Book.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Book " + i)
                    .desc("Book Desc " + i)
                    .imageUrl("http://image_book_" + i)
                    .status((i & 1) == 0 ? BookStatus.BORROWED : BookStatus.AVAILABLE)
                    .lastUpdateDt(ZonedDateTime.now().minusDays(i))
                    .build();
            bookRepository.save(book);
        }
    }

    private Book setupExistingBookWithRecords() {
        Book book = setupExistingBook();
        BorrowRecord record = BorrowRecord.builder()
                .username("user999")
                .borrowTimestamp(System.currentTimeMillis())
                .returnTimestamp(System.currentTimeMillis())
                .build();
        book.setBorrowRecords(new ArrayList<>(List.of(record)));
        return bookRepository.save(book);
    }

    private Book setupExistingBook() {
        Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .name("Book 001")
                .desc("Book Desc 001")
                .imageUrl("http://image_book_001")
                .status(BookStatus.AVAILABLE)
                .borrowRecords(null)
                .lastUpdateDt(ZonedDateTime.now().minusDays(1))
                .build();
        return bookRepository.save(book);
    }

    private String authorizationToken(String userPass) {
        String base64EncodedToken = Base64.getEncoder().encodeToString(
                userPass.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64EncodedToken;
    }

    private String absoluteUrl(String path) {
        return "http://localhost:" + port + "/v1/member" + path;
    }
}
