package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.entity.Book;
import posmy.interview.boot.entity.BorrowRecord;
import posmy.interview.boot.enums.BookStatus;
import posmy.interview.boot.error.BookUnavailableException;
import posmy.interview.boot.model.request.BookBorrowRequest;
import posmy.interview.boot.repos.BookRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookBorrowServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookBorrowService bookBorrowService;

    @Captor
    private final ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

    private BookBorrowRequest request;
    private Book existingBook;

    @BeforeEach
    void setup() {
        String bookId = "book001-1";
        String username = "user005";
        request = BookBorrowRequest.builder()
                .bookId(bookId)
                .username(username)
                .build();
        existingBook = Book.builder()
                .id(bookId)
                .name("book 1")
                .desc("book 1 desc")
                .imageUrl("http://image")
                .status(BookStatus.AVAILABLE)
                .borrowRecords(new ArrayList<>())
                .lastUpdateDt(ZonedDateTime.now())
                .build();
    }

    @Test
    void givenIdAndUsernameThenUpdateBookStatusBorrowedAndRecord() {
        BorrowRecord expectedRecord = BorrowRecord.builder()
                .username(request.getUsername())
                .build();
        Book expectedBook = existingBook.toBuilder()
                .status(BookStatus.BORROWED)
                .borrowRecords(new ArrayList<>(List.of(expectedRecord)))
                .build();

        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        bookBorrowService.execute(request);
        verify(bookRepository, times(1))
                .save(bookCaptor.capture());
        assertThat(bookCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @Test
    void givenBookUnavailableThenThrowBookUnavailableException() {
        existingBook.setStatus(BookStatus.BORROWED);
        BorrowRecord expectedRecord = BorrowRecord.builder()
                .username(request.getUsername())
                .build();
        Book expectedBook = existingBook.toBuilder()
                .status(BookStatus.BORROWED)
                .borrowRecords(new ArrayList<>(List.of(expectedRecord)))
                .build();

        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        assertThrows(BookUnavailableException.class,
                () -> bookBorrowService.execute(request));
        verify(bookRepository, times(0))
                .save(bookCaptor.capture());
    }
}