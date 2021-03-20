package posmy.interview.boot.integration_test;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import posmy.interview.boot.BaseAPIIntegrationTest;
import posmy.interview.boot.dto.BookCreateDto;
import posmy.interview.boot.dto.BookDto;
import posmy.interview.boot.dto.BookUpdateDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.fixture.BookBuilder;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BookAPITest extends BaseAPIIntegrationTest {

    /*
        Librarian
     */
    @Test
    public void librarianCreateBookTest() {
        headers.add(AUTHORIZATION, getLibrarian().getToken());
        BookCreateDto createDto = BookBuilder.sample().buildCreateDto();
        ResponseEntity<BookDto> response = restTemplate.exchange("/book", POST, new HttpEntity(createDto, headers), BookDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).id > 0);
    }

    @Test
    public void librarianUpdateBookTest() {
        BookDto bookDto = genericCreateBook();

        BookUpdateDto updateDto = BookBuilder.sample().setTitle("Update Title").buildUpdateDto();
        ResponseEntity<BookDto> response2 = restTemplate.exchange("/book/" + bookDto.id, PUT, new HttpEntity(updateDto, headers), BookDto.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(Objects.requireNonNull(response2.getBody()).title, updateDto.title);
    }

    @Test
    public void librarianDeleteBookTest() {
        BookDto bookDto = genericCreateBook();

        ResponseEntity<Void> response2 = restTemplate.exchange("/book/" + bookDto.id, DELETE, new HttpEntity(headers), Void.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());

        ResponseEntity<BookDto> response3 = restTemplate.exchange("/book" + bookDto.id, GET, new HttpEntity(headers), BookDto.class);
        assertEquals(NOT_FOUND, response3.getStatusCode());
    }

    /*
        Member
     */

    @Test
    public void memberCreateBookTest() {
        headers.add(AUTHORIZATION, getMember().getToken());
        BookCreateDto createDto = BookBuilder.sample().buildCreateDto();
        ResponseEntity<BookDto> response = restTemplate.exchange("/book", POST, new HttpEntity(createDto, headers), BookDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void memberUpdateBookTest() {
        BookDto bookDto = genericCreateBook();

        headers.set(AUTHORIZATION, getMember().getToken());
        BookUpdateDto updateDto = BookBuilder.sample().setTitle("Update Title").buildUpdateDto();
        ResponseEntity<BookDto> response2 = restTemplate.exchange("/book/" + bookDto.id, PUT, new HttpEntity(updateDto, headers), BookDto.class);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }


    @Test
    public void memberDeleteBookTest() {
        BookDto bookDto = genericCreateBook();

        headers.set(AUTHORIZATION, getMember().getToken());
        ResponseEntity<Void> response2 = restTemplate.exchange("/book/" + bookDto.id, DELETE, new HttpEntity(headers), Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    public void memberBorrowBookTest() {
        BookDto bookDto = genericCreateBook();
        headers.set(AUTHORIZATION, getMember().getToken());
        ResponseEntity<BookDto> response2 = restTemplate.exchange("/book/" + bookDto.id + "/borrow", POST, new HttpEntity(headers), BookDto.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(Book.Status.BORROWED.name(), Objects.requireNonNull(response2.getBody()).status);
    }


    @Test
    public void memberReturnBookTest() {
        BookDto bookDto = genericCreateBook();

        headers.set(AUTHORIZATION, getMember().getToken());
        ResponseEntity<BookDto> response2 = restTemplate.exchange("/book/" + bookDto.id + "/borrow", POST, new HttpEntity(headers), BookDto.class);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(Book.Status.BORROWED.name(), Objects.requireNonNull(response2.getBody()).status);

        ResponseEntity<BookDto> response3 = restTemplate.exchange("/book/" + bookDto.id + "/return", POST, new HttpEntity(headers), BookDto.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertEquals(Book.Status.AVAILABLE.name(), Objects.requireNonNull(response3.getBody()).status);
    }

    private BookDto genericCreateBook() {
        headers.add(AUTHORIZATION, getLibrarian().getToken());
        BookCreateDto createDto = BookBuilder.sample().buildCreateDto();
        ResponseEntity<BookDto> response = restTemplate.exchange("/book", POST, new HttpEntity(createDto, headers), BookDto.class);
        BookDto bookDto = response.getBody();
        assert bookDto != null;
        return bookDto;
    }


}
