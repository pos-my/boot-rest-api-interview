package posmy.interview.boot.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import posmy.interview.boot.dto.request.SearchBookDto;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.exception.ValidationException;
import posmy.interview.boot.repository.BookRepository;
import posmy.interview.boot.testutils.factories.BookFactory;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Should get a list of book in page")
    void shouldGetBooks() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> books = BookFactory.getInstance().constructListOfBook(BookStatus.AVAILABLE);

        Page<Book> mockBookPage = new PageImpl<>(
            books,
            pageRequest,
            books.size()
        );

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(mockBookPage);

        Page<Book> bookPage = bookService.getBooks(
            SearchBookDto.builder().title("title").build(),
            pageRequest
        );

        Assertions.assertEquals(bookPage.getTotalElements(), mockBookPage.getTotalElements());
        Assertions.assertEquals(bookPage.getTotalPages(), mockBookPage.getTotalPages());
        Assertions.assertEquals(bookPage.get().findFirst(), mockBookPage.get().findFirst());
    }

    @Test
    @DisplayName("Should create a book")
    void shouldCreateBook() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookRepository.save(any())).thenReturn(mockBook);

        bookService.createBook(BookFactory.getInstance().constructCreateBookDto());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should update a book")
    void shouldUpdateBook() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));
        when(bookRepository.save(any())).thenReturn(mockBook);

        bookService.updateBook(1L, BookFactory.getInstance().constructUpdateBookDto());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should delete a book")
    void shouldDeleteBook() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(any(Book.class));
    }

    @Test
    @DisplayName("Should borrow a book")
    void shouldBorrowBook() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);
        Book mockBorrowedBook = BookFactory.getInstance().constructBook(BookStatus.BORROWED);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));
        when(bookRepository.save(any())).thenReturn(mockBorrowedBook);

        Book book = bookService.borrowBook(1L);

        verify(bookRepository, times(1)).save(any(Book.class));
        Assertions.assertEquals(BookStatus.BORROWED, book.getStatus());
    }

    @Test
    @DisplayName("Should not borrow a book if not available")
    void shouldNotBorrowBookIfNotAvailable() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.BORROWED);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        Assertions.assertThrows(
            ValidationException.class,
            () -> bookService.borrowBook(1L)
        );
    }

    @Test
    @DisplayName("Should return a book")
    void shouldReturnBook() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.BORROWED);
        Book mockReturnedBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));
        when(bookRepository.save(any())).thenReturn(mockReturnedBook);

        Book book = bookService.returnBook(1L);

        verify(bookRepository, times(1)).save(any(Book.class));
        Assertions.assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }

    @Test
    @DisplayName("Should not return a book if returned already")
    void shouldNotReturnBookIfReturned() {
        Book mockBook = BookFactory.getInstance().constructBook(BookStatus.AVAILABLE);

        when(bookRepository.findById(any())).thenReturn(Optional.of(mockBook));

        Assertions.assertThrows(
            ValidationException.class,
            () -> bookService.returnBook(1L)
        );
    }
}
