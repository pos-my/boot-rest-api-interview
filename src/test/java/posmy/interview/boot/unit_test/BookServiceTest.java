package posmy.interview.boot.unit_test;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import posmy.interview.boot.BaseTest;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.fixture.BookBuilder;
import posmy.interview.boot.mockauth.WithMockCustomUser;
import posmy.interview.boot.services.BookService;

import javax.persistence.EntityNotFoundException;

import static org.junit.Assert.*;

@SpringBootTest
@ContextConfiguration
public class BookServiceTest extends BaseTest {

    @Autowired
    private BookService bookService;

    @WithMockCustomUser(authorities = {"BOOK_CREATE"})
    @Test
    public void createTest() {
        Book book = bookService.create(BookBuilder.sample().build());
        assertTrue(book.getId() > 0);
    }

    @WithMockCustomUser(authorities = {"BOOK_CREATE", "BOOK_UPDATE"})
    @Test
    public void updateTest() {
        String updateTitle = "Update Title";
        Book book = bookService.create(BookBuilder.sample().build());
        book.setTitle(updateTitle);
        book = bookService.update(book);
        assertEquals(updateTitle, book.getTitle());
    }

    @WithMockCustomUser(authorities = {"BOOK_CREATE", "BOOK_READ", "BOOK_DELETE"})
    @Test
    public void deleteTest() {
        Book book = bookService.create(BookBuilder.sample().build());
        bookService.delete(book.getId());
        try {
            bookService.getById(book.getId());
        } catch (EntityNotFoundException e) {
            return;
        }
        fail();
    }

    /*
        Positive test case
     */
    @WithMockCustomUser(authorities = {"BOOK_CREATE", "BOOK_BORROW"})
    @Test
    public void borrowTest() {
        Book book = bookService.create(BookBuilder.sample().build());
        try {
            book = bookService.borrowBook(book.getId());
        } catch (Exception e) {
            fail();
        }
        assertEquals(book.getStatus(), Book.Status.BORROWED);
    }

    @WithMockCustomUser(authorities = {"BOOK_CREATE", "BOOK_BORROW", "BOOK_RETURN"})
    @Test
    public void returnTest() {
        Book book = bookService.create(BookBuilder.sample().build());
        try {
            book = bookService.borrowBook(book.getId());
            book = bookService.returnBook(book.getId());
        } catch (IllegalStateException e) {
            fail();
        }
        assertEquals(book.getStatus(), Book.Status.AVAILABLE);
    }

    /*
        Negative test case
     */

    @WithMockCustomUser(authorities = {"BOOK_CREATE", "BOOK_BORROW"})
    @Test
    public void borrowTestFailed() {
        Book book = bookService.create(BookBuilder.sample().build());
        try {
            book = bookService.borrowBook(book.getId());
        } catch (IllegalStateException e) {
            fail();
        }
        try {
            book = bookService.borrowBook(book.getId());
        } catch (IllegalStateException e) {
            return;
        }
        fail();
    }

}
