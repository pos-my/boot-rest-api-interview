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
import posmy.interview.boot.error.BookAlreadyReturnedException;
import posmy.interview.boot.error.BorrowRecordMismatchException;
import posmy.interview.boot.model.request.BookReturnRequest;
import posmy.interview.boot.repos.BookRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookReturnServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookReturnService bookReturnService;

    @Captor
    private final ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

    private BookReturnRequest request;
    private BorrowRecord borrowRecord;
    private Book existingBook;
    private BorrowRecord expectedRecord;
    private Book expectedBook;

    @BeforeEach
    void setup() {
        String bookId = "book001-1";
        String username = "username";
        request = BookReturnRequest.builder()
                .bookId(bookId)
                .username(username)
                .build();
        borrowRecord = BorrowRecord.builder()
                .id(2L)
                .username(username)
                .borrowTimestamp(System.currentTimeMillis() - 3600*1000)
                .returnTimestamp(null)
                .build();
        existingBook = Book.builder()
                .id(bookId)
                .name("book 1")
                .desc("book 1 desc")
                .imageUrl("http://image")
                .status(BookStatus.BORROWED)
                .borrowRecords(new ArrayList<>(List.of(borrowRecord)))
                .lastUpdateDt(ZonedDateTime.now())
                .build();
        expectedRecord = borrowRecord.toBuilder()
                .isReturn(true)
                .build();
        expectedBook = existingBook.toBuilder()
                .status(BookStatus.AVAILABLE)
                .borrowRecords(new ArrayList<>(List.of(expectedRecord)))
                .build();
    }

    @Test
    void givenIdAndUsernameThenReturnBook() {
        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        bookReturnService.execute(request);
        verify(bookRepository, times(1))
                .save(bookCaptor.capture());
        assertThat(bookCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @Test
    void givenIdAndUsernameWithMultipleRecordsThenReturnBook() {
        setupMultipleRecords();

        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        bookReturnService.execute(request);
        verify(bookRepository, times(1))
                .save(bookCaptor.capture());
        assertThat(bookCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @Test
    void givenBookNotBorrowedThenThrowBookAlreadyReturnedException() {
        existingBook.setStatus(BookStatus.AVAILABLE);
        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        assertThrows(BookAlreadyReturnedException.class,
                () -> bookReturnService.execute(request));
        verify(bookRepository, times(0))
                .save(bookCaptor.capture());
    }

    @Test
    void givenBookBorrowedByDifferentUsernameThenThrowBorrowRecordMismatchException() {
        existingBook.getBorrowRecords().get(0).setUsername("otherUsername");
        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        assertThrows(BorrowRecordMismatchException.class,
                () -> bookReturnService.execute(request));
        verify(bookRepository, times(0))
                .save(bookCaptor.capture());
    }

    @Test
    void givenBorrowRecordAlreadyIsReturnTrueThenThrowBorrowRecordMismatchException() {
        existingBook.getBorrowRecords().get(0).setIsReturn(true);
        when(bookRepository.findById(request.getBookId()))
                .thenReturn(Optional.of(existingBook));

        assertThrows(BorrowRecordMismatchException.class,
                () -> bookReturnService.execute(request));
        verify(bookRepository, times(0))
                .save(bookCaptor.capture());
    }

    private void setupMultipleRecords() {
        borrowRecord.setBorrowTimestamp(System.currentTimeMillis());
        BorrowRecord borrowRecord2 = BorrowRecord.builder()
                .id(3L)
                .borrowTimestamp(System.currentTimeMillis() - 7200*1000)
                .returnTimestamp(System.currentTimeMillis() - 7200*1000)
                .username("username2")
                .isReturn(true)
                .build();
        BorrowRecord borrowRecord3 = BorrowRecord.builder()
                .id(4L)
                .borrowTimestamp(System.currentTimeMillis() - 1800*1000)
                .returnTimestamp(System.currentTimeMillis() - 1800*1000)
                .username("username3")
                .isReturn(true)
                .build();
        existingBook.setBorrowRecords(new ArrayList<>(List.of(borrowRecord3, borrowRecord, borrowRecord2)));

        expectedRecord = borrowRecord.toBuilder()
                .isReturn(true)
                .build();
        expectedBook = existingBook.toBuilder()
                .status(BookStatus.AVAILABLE)
                .borrowRecords(new ArrayList<>(List.of(
                        borrowRecord3.toBuilder().build(),
                        expectedRecord,
                        borrowRecord2.toBuilder().build())))
                .build();
    }
}