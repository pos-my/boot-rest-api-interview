package posmy.interview.boot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MemberBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private MemberBookService memberBookService = new MemberBookService();

    @DisplayName("Test acquire available book list")
    @Test
    void testViewAvailable() {
        Mockito.when(bookRepository.findAllByDeleted(false)).thenReturn(List.of(
                Book.builder().id(1L).state(Book.State.AVAILABLE).build(),
                Book.builder().id(2L).state(Book.State.BORROWED).build()
        ));
        List<Book> sampleBookList = memberBookService.viewAll();
        Assertions.assertEquals(2, sampleBookList.size());
    }

    @DisplayName("Test borrowing borrowed")
    @Test
    void testBorrowBorrowed() {

        Optional<Book> maybeBook = memberBookService.borrowBook(11L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isEmpty());
    }

    @DisplayName("Test borrowing available")
    @Test
    void testBorrowAvailable() {

        final Date createdOn = new Date();
        final Book currentBook = Book.builder()
                .id(11L)
                .state(Book.State.AVAILABLE)
                .deleted(false)
                .createdOn(createdOn)
                .build();
        final Book updatedBook = Book.builder()
                .id(11L)
                .state(Book.State.BORROWED)
                .deleted(false)
                .createdOn(createdOn)
                .build();

        Mockito.when(bookRepository.findByIdAndStateAndDeleted(11L, Book.State.AVAILABLE, false))
                .thenReturn(Optional.of(currentBook));
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        Optional<Book> maybeBook = memberBookService.borrowBook(11L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isPresent());
        Assertions.assertEquals(Book.State.BORROWED, maybeBook.get().getState()); // returning new state
    }

    @DisplayName("Test returning available")
    @Test
    void testReturnAvailable() {
        Optional<Book> maybeBook = memberBookService.returnBook(22L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isEmpty());
    }

    @DisplayName("Test returning borrowed")
    @Test
    void testReturnBorrowed() {
        final Date createdOn = new Date();
        final Book currentBook = Book.builder()
                .id(22L)
                .state(Book.State.BORROWED)
                .deleted(false)
                .createdOn(createdOn)
                .build();
        final Book updatedBook = Book.builder()
                .id(22L)
                .state(Book.State.AVAILABLE)
                .deleted(false)
                .createdOn(createdOn)
                .build();

        Mockito.when(bookRepository.findByIdAndStateAndDeleted(22L, Book.State.BORROWED, false))
                .thenReturn(Optional.of(currentBook));
        Mockito.when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        Optional<Book> maybeBook = memberBookService.returnBook(22L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isPresent());
        Assertions.assertEquals(Book.State.AVAILABLE, maybeBook.get().getState()); // returning new state
    }

    @DisplayName("Test find non-existing book")
    @Test
    void testFindNonExistBook() {
        Optional<Book> maybeBook = memberBookService.get(33L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isEmpty());
    }

    @DisplayName("Test find existing borrowed book")
    @Test
    void testFindBorrowedBook() {

        Mockito.when(bookRepository.findByIdAndDeleted(33L, false))
                .thenReturn(
                        Optional.of(
                                Book.builder()
                                        .id(33L)
                                        .state(Book.State.BORROWED)
                                        .deleted(false)
                                        .createdOn(new Date())
                                        .build()
                        )
                );

        Optional<Book> maybeBook = memberBookService.get(33L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isPresent());
        Assertions.assertEquals(Book.State.BORROWED, maybeBook.get().getState());
    }

    @DisplayName("Test find existing available book")
    @Test
    void testFindAvailableBook() {
        Mockito.when(bookRepository.findByIdAndDeleted(34L, false))
                .thenReturn(
                        Optional.of(
                                Book.builder()
                                        .id(34L)
                                        .state(Book.State.AVAILABLE)
                                        .deleted(false)
                                        .createdOn(new Date())
                                        .build()
                        )
                );

        Optional<Book> maybeBook = memberBookService.get(34L);
        Assertions.assertNotNull(maybeBook);
        Assertions.assertTrue(maybeBook.isPresent());
        Assertions.assertEquals(Book.State.AVAILABLE, maybeBook.get().getState());
    }

}
