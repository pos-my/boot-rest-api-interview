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

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LibrarianBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibrarianBookService librarianBookService = new LibrarianBookService();


    @DisplayName("Test view book list")
    @Test
    void testViewAvailable() {
        Mockito.when(bookRepository.findAll()).thenReturn(List.of(
                Book.builder().id(1L).state(Book.State.AVAILABLE).deleted(false).build(),
                Book.builder().id(2L).state(Book.State.BORROWED).deleted(false).build(),
                Book.builder().id(3L).state(Book.State.AVAILABLE).deleted(true).build(),
                Book.builder().id(4L).state(Book.State.BORROWED).deleted(true).build()
        ));
        List<Book> sampleBookList = librarianBookService.viewAll();
        Assertions.assertEquals(4, sampleBookList.size());
    }

    @DisplayName("Test view empty book list")
    @Test
    void testViewEmptyAvailable() {

        List<Book> sampleBookList = librarianBookService.viewAll();
        Assertions.assertEquals(0, sampleBookList.size());
    }

    @DisplayName("Test get book")
    @Test
    void testGetBook() {
        Mockito.when(bookRepository.findById(12L))
                .thenReturn(Optional.of(
                        Book.builder().id(12L).state(Book.State.AVAILABLE).deleted(false).build()
                ));
        Optional<Book> maybeBook = librarianBookService.get(12L);
        Assertions.assertTrue(maybeBook.isPresent());
        Assertions.assertFalse(maybeBook.get().isDeleted());
    }

    @DisplayName("Test get non-existing book")
    @Test
    void testGetNonExistBook() {

        Optional<Book> maybeBook = librarianBookService.get(13L);
        Assertions.assertTrue(maybeBook.isEmpty());
    }

    @DisplayName("Test add new book")
    @Test
    void testAddNewBook() {
        Book newBook = Book.builder()
                .id(1L)
                .deleted(false)
                .build();
        Mockito.when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(newBook);

        Book createdBook = librarianBookService.add(newBook);

        Assertions.assertNotNull(createdBook);
        Assertions.assertFalse(createdBook.isDeleted());
    }

    @DisplayName("Test update book")
    @Test
    void testUpdateBook() {

        Book changeBook = Book.builder()
                .id(1L)
                .deleted(true)
                .build();
        Mockito.when(bookRepository.save(changeBook))
                .thenReturn(changeBook);

        Book updateBook = librarianBookService.update(changeBook);

        Assertions.assertNotNull(updateBook);
        Assertions.assertTrue(updateBook.isDeleted());
    }

    @DisplayName("Test delete book")
    @Test
    void testDeleteBook() {

        Book currentBook = Book.builder()
                .id(1L)
                .deleted(false)
                .build();
        Book changeBook = Book.builder()
                .id(1L)
                .deleted(true)
                .build();
        Mockito.when(bookRepository.findById(1L))
                .thenReturn(Optional.of(currentBook));
        Mockito.when(bookRepository.save(changeBook))
                .thenReturn(changeBook);

        Optional<Book> updateBook = librarianBookService.delete(1L);

        Assertions.assertNotNull(updateBook);
        Assertions.assertTrue(updateBook.isPresent());
        Assertions.assertTrue(updateBook.get().isDeleted());
    }

    @DisplayName("Test delete deleted book")
    @Test
    void testDeleteDeletedBook() {

        Mockito.when(bookRepository.findById(2L))
                .thenReturn(Optional.of(
                        Book.builder()
                                .id(2L)
                                .deleted(true)
                                .build()
                ));


        Optional<Book> updateBook = librarianBookService.delete(2L);

        Assertions.assertNotNull(updateBook);
        Assertions.assertTrue(updateBook.isPresent());
        Assertions.assertTrue(updateBook.get().isDeleted());
    }

    @DisplayName("Test delete non-existing book")
    @Test
    void testDeleteNonExistBook() {

        Optional<Book> updateBook = librarianBookService.delete(3L);

        Assertions.assertNotNull(updateBook);
        Assertions.assertTrue(updateBook.isEmpty());
    }

}
